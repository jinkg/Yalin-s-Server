package com.yalin.fidouaf.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.ebayopensource.fido.uaf.storage.AuthenticatorRecord;
import org.ebayopensource.fido.uaf.storage.DuplicateKeyException;
import org.ebayopensource.fido.uaf.storage.RegistrationRecord;

public class DBHelper {

	private static Connection connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		String url = "jdbc:mysql://localhost:3306/uafdb";
		Connection conn;
		try {
			conn = DriverManager.getConnection(url, "root", "");
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addRegRecords(RegistrationRecord[] records) throws DuplicateKeyException {
		if (records != null && records.length > 0) {
			for (int i = 0; i < records.length; i++) {
				if (indexKeyExist(records[i].authenticator.toString())) {
					throw new DuplicateKeyException();
				}
				addRegRecord(records[i]);
			}
		}
	}

	public static boolean indexKeyExist(String indexKey) {
		Connection conn = connectDB();

		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(
					"SELECT * FROM  RegRecords WHERE " + RegistrationRecord.KEY_REG_INDEXID + "= '" + indexKey + "'");
			rs = pst.executeQuery();
			conn.commit();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@SuppressWarnings("resource")
	public static void addRegRecord(RegistrationRecord record) {
		Connection conn = connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("INSERT INTO Authenticator(" + AuthenticatorRecord.KEY_AUTH_AAID + ","
					+ AuthenticatorRecord.KEY_AUTH_KEYID + "," + AuthenticatorRecord.KEY_AUTH_DEVICE_ID + ","
					+ AuthenticatorRecord.KEY_AUTH_USERNAME + "," + AuthenticatorRecord.KEY_AUTH_STATUS
					+ ") VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, record.authenticator.AAID);
			pst.setString(2, record.authenticator.KeyID);
			pst.setString(3, record.authenticator.deviceId);
			pst.setString(4, record.authenticator.username);
			pst.setString(5, record.authenticator.status);

			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			int authId = -1;
			if (rs.next()) {
				authId = rs.getInt(1);
			}
			if (authId == -1) {
				throw new SQLException();
			}
			pst = conn.prepareStatement("INSERT INTO RegRecords(" + RegistrationRecord.KEY_REG_INDEXID + ","
					+ RegistrationRecord.KEY_REG_AUTHID + "," + RegistrationRecord.KEY_REG_PUBLIC_KEY + ","
					+ RegistrationRecord.KEY_REG_SIGN_COUNTER + "," + RegistrationRecord.KEY_REG_AUTH_VERSION + ","
					+ RegistrationRecord.KEY_REG_PNG + "," + RegistrationRecord.KEY_REG_USERNAME + ","
					+ RegistrationRecord.KEY_REG_USER_ID + "," + RegistrationRecord.KEY_REG_DEVICE_ID + ","
					+ RegistrationRecord.KEY_REG_TIME_STAMP + "," + RegistrationRecord.KEY_REG_STATUS + ","
					+ RegistrationRecord.KEY_REG_AC + "," + RegistrationRecord.KEY_REG_ADTS + ","
					+ RegistrationRecord.KEY_REG_AS + "," + RegistrationRecord.KEY_REG_AVS
					+ ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pst.setString(1, record.authenticator.toString());
			pst.setInt(2, authId);
			pst.setString(3, record.PublicKey);
			pst.setString(4, record.SignCounter);
			pst.setString(5, record.AuthenticatorVersion);
			pst.setString(6, record.tcDisplayPNGCharacteristics);
			pst.setString(7, record.username);
			pst.setString(8, record.userId);
			pst.setString(9, record.deviceId);
			pst.setString(10, record.timeStamp);
			pst.setString(11, record.status);
			pst.setString(12, record.attestCert);
			pst.setString(13, record.attestDataToSign);
			pst.setString(14, record.attestSignature);
			pst.setString(15, record.attestVerifiedStatus);

			pst.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	public static RegistrationRecord readRecord(String indexKey) {
		Connection conn = connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		RegistrationRecord record = null;
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(
					"SELECT * FROM  RegRecords WHERE " + RegistrationRecord.KEY_REG_INDEXID + "= '" + indexKey + "'");
			rs = pst.executeQuery();
			if (rs.next()) {
				record = new RegistrationRecord();
				int authId = rs.getInt(rs.findColumn(RegistrationRecord.KEY_REG_AUTHID));

				pst = conn.prepareStatement("SELECT * FROM  Authenticator WHERE id= '" + authId + "'");
				rs1 = pst.executeQuery();

				AuthenticatorRecord authenticator = new AuthenticatorRecord();
				if (rs1.next()) {
					authenticator.AAID = rs1.getString(rs1.findColumn(AuthenticatorRecord.KEY_AUTH_AAID));
					authenticator.KeyID = rs1.getString(rs1.findColumn(AuthenticatorRecord.KEY_AUTH_KEYID));
					authenticator.deviceId = rs1.getString(rs1.findColumn(AuthenticatorRecord.KEY_AUTH_DEVICE_ID));
					authenticator.username = rs1.getString(rs1.findColumn(AuthenticatorRecord.KEY_AUTH_USERNAME));
					authenticator.status = rs1.getString(rs1.findColumn(AuthenticatorRecord.KEY_AUTH_STATUS));
				}

				record.authenticator = authenticator;
				record.PublicKey = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_PUBLIC_KEY));
				record.SignCounter = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_SIGN_COUNTER));
				record.AuthenticatorVersion = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_AUTH_VERSION));
				record.tcDisplayPNGCharacteristics = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_PNG));
				record.username = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_USERNAME));
				record.userId = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_USER_ID));
				record.deviceId = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_DEVICE_ID));
				record.timeStamp = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_TIME_STAMP));
				record.status = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_STATUS));
				record.attestCert = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_AC));
				record.attestDataToSign = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_ADTS));
				record.attestSignature = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_AS));
				record.attestVerifiedStatus = rs.getString(rs.findColumn(RegistrationRecord.KEY_REG_AVS));
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return record;
	}

	@SuppressWarnings("resource")
	public static void delete(String indexKey) {
		Connection conn = connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			pst = conn.prepareStatement(
					"SELECT * FROM  RegRecords WHERE " + RegistrationRecord.KEY_REG_INDEXID + "= '" + indexKey + "'");
			rs = pst.executeQuery();
			if (rs.next()) {
				int authId = rs.getInt(rs.findColumn(RegistrationRecord.KEY_REG_AUTHID));
				pst = conn.prepareStatement("DELTE * from Authenticator where id='" + authId + "'");
				pst.executeUpdate();
			}
			pst.executeUpdate("DELTE * from RegRecords where indexKey='" + indexKey + "'");
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
