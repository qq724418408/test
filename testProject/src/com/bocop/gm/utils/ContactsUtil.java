package com.bocop.gm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bocop.gm.bean.ContactsInfo;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Log;

/**
 * 通讯录
 * 
 * @author xxq
 *
 *         2015年12月16日
 */
public class ContactsUtil {
	/** TAG */
	public static final String TAG = "TAG";
	/** ID CONTACTS表中的Data ._ID */
	public static final String ID = "Id";
	/** 名字 */
	public static final String NAME = "UpdateNma";
	/** 地址 */
	public static final String ADDRESS = "UpdateAddress";
	/** 邮箱 */
	public static final String EMAIL = "UpdateEmail";
	/** 手机 */
	public static final String PHONE = "UpdatePhone";
	/** 昵称 */
	public static final String NICKNAME = "UpdateNICKNAME";
	/** 通话类型 */
	public static final String CALLTYPE = "CALLTYPE";
	/** 通话日期 */
	public static final String CALLTIME = "CALLTIME";
	/** 分组 Id */
	public static final String GROUP = "UpdateGROUP";
	/** 机构组织 */
	public static final String ORGANIZATION = "ORGANIZATION";
	/** 类型 */
	private static final String TYPE = "mimetype";
	/** URI */
	private static final String URI = "content://com.android.contacts/contacts";
	/** 地址 TYPE */
	private static final String QUERYADDRESS = "vnd.android.cursor.item/postal-address_v2";
	/** 邮箱 TYPE */
	private static final String QUERYEMAIL = "vnd.android.cursor.item/email_v2";
	/** 名字 TYPE */
	private static final String QUERYNAME = "vnd.android.cursor.item/name";
	/** 组织 TYPE */
	private static final String QUERYORGANIZATION = "vnd.android.cursor.item/organization";
	/** 昵称 TYPE */
	private static final String QUERYNICKNAME = "vnd.android.cursor.item/nickname";
	/** 分组 TYPE */
	private static final String QUERYGROUP = "vnd.android.cursor.item/group_membership";
	/** Phone TYPE */
	private static final String QUERYPHONE = "vnd.android.cursor.item/phone_v2";
	/** URI */
	private static final String QUERY_FILTER_CRITERIA = "content://com.android.contacts/data/phones/filter/";
	/** 查询结果集 */
	private static ArrayList<ContentValues> queryList = null;
	/** 更新数据 */
	private static ArrayList<String> listdata;
	/** 更新下标 */
	private static ArrayList<String> listInsertIndex;

	/**
	 * 查询单个联系人 支持模糊搜索.输入联系人 支持单字,拼音,号码
	 * 
	 * @param context
	 *            上下文
	 * @param criteria
	 *            查询条件 name or phoneNum
	 * @return
	 */
	public static ArrayList<ContentValues> searchContacts(Context context, String criteria) {
		Uri uri = Uri.parse(QUERY_FILTER_CRITERIA + criteria);
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { Data.DISPLAY_NAME, Data._ID, Data.DATA1 }, null, null, null);
		if (queryList == null) {
			queryList = new ArrayList<ContentValues>();
		} else
			queryList.clear();
		while (cursor.moveToNext()) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(NAME, cursor.getString(0));
			contentValues.put(PHONE, cursor.getString(2));
			queryList.add(contentValues);
		}
		cursor.close();
		return queryList;
	}

	/**
	 * 查询所有联系人
	 * 
	 * @param context
	 * @param queryValues
	 *            查询字段数组 String[]{NAME,ADDRESS}
	 * @return
	 */
	public static List<ContentValues> findLinkmanInfo(Context context, String[] queryValues) {
		if (queryList == null) {
			queryList = new ArrayList<ContentValues>();
		} else
			queryList.clear();
		ContentValues values = new ContentValues();
		for (int i = 0; i < queryValues.length; i++) {
			values.put(queryValues[i], "" + i);
		}
		Uri uri = Uri.parse(URI); // 访问raw_contacts表
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { Data._ID, ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER, Contacts._ID }, null, null, null); //
		while (cursor.moveToNext()) {
			ContentValues queryResultValues = new ContentValues();
			Log.i("TAG", "contacts_ID+" + cursor.getInt(3));
			queryResultValues.put(ID, cursor.getInt(3) + "");
			// 获得联系人姓名
			String displayName = cursor.getString(1);
			if (values.getAsString(NAME) != null) {
				queryResultValues.put(NAME, displayName);
			}
			if (cursor.getInt(2) > 0 && values.getAsString(PHONE) != null) {
				boolean isAdd = false;// 是否追加"/"
				Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + cursor.getInt(0), null, null);
				if (phoneCursor.moveToFirst()) {
					String data = "";
					do {
						if (isAdd) {
							data += "/";
						}
						// phone
						String phoneNumber = phoneCursor
								.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						data += phoneNumber;
						isAdd = true;
					} while (phoneCursor.moveToNext());
					if (values.getAsString(PHONE) != null) {
						queryResultValues.put(PHONE, data);
					}
				}

			}

			/************************************/
			int id = cursor.getInt(0);// 获得id并且在data中寻找数据
			// if (values.getAsString(ID) != null) {
			// queryResultValues.put(ID, id);
			// }
			uri = Uri.parse(URI + "/" + id + "/data"); // 如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data
			Cursor cursor2 = resolver.query(uri, new String[] { Data.DATA1, Data.MIMETYPE }, null, null, null); // data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
			while (cursor2.moveToNext()) {
				String data = cursor2.getString(cursor2.getColumnIndex("data1"));
				if (cursor2.getString(cursor2.getColumnIndex(TYPE)).equals(QUERYEMAIL)
						&& values.getAsString(EMAIL) != null) { // 如果是email
					if (data == null || "".equals(data)) {
						data = "null";
					}
					queryResultValues.put(EMAIL, data);
				} else if (cursor2.getString(cursor2.getColumnIndex(TYPE)).equals(QUERYADDRESS)
						&& values.getAsString(ADDRESS) != null) { // 如果是地址
					if (data == null || "".equals(data)) {
						data = "null";
					}
					queryResultValues.put(ADDRESS, data);
				} else if (cursor2.getString(cursor2.getColumnIndex(TYPE)).equals(QUERYNICKNAME)) {// 是昵称
					queryResultValues.put(NICKNAME, data);
				} else if (cursor2.getString(cursor2.getColumnIndex(TYPE)).equals(QUERYGROUP)) {// 是分组
					queryResultValues.put(GROUP, data);
				} else if (cursor2.getString(cursor2.getColumnIndex(TYPE)).equals(QUERYORGANIZATION)) {// 是组织
					queryResultValues.put(ORGANIZATION, data);
				}
			}
			queryList.add(queryResultValues);
			cursor2.close();
		}
		cursor.close();// 关闭游标
		return queryList;
	}

	/**
	 * 获取所有拥有手机号的联系人
	 * 
	 * @param context
	 * @return
	 */
	public static List<ContactsInfo> getAllPhoneContacts(Context context) {
		List<ContactsInfo> listContacts = new ArrayList<ContactsInfo>();
		ContentResolver cr = context.getContentResolver();
		String[] mContactsProjection = new String[] { ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.SORT_KEY_PRIMARY };

		String phoneNum;
		String name;
		String sortKey;

		// 查询contacts表中的所有数据
		Cursor cursor = cr.query(Phone.CONTENT_URI, mContactsProjection, null, null,
				ContactsContract.Contacts.SORT_KEY_PRIMARY);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				phoneNum = cursor.getString(1);
				name = cursor.getString(2).replaceAll("'", "").replaceAll("\"", "");
				sortKey = getSortKey(cursor.getString(3));

				boolean isExits = false;
				for (int i = 0; i < listContacts.size(); i++) {
					if (name.equals(listContacts.get(i).getN())) {
						isExits = true;
					}
				}
				// 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
				phoneNum = phoneNum.replaceAll("^(\\+86)", "");
				phoneNum = phoneNum.replaceAll("^(86)", "");
				phoneNum = phoneNum.replaceAll("-", "");
				phoneNum = phoneNum.replaceAll(" ", "");
				phoneNum = phoneNum.trim();
				// 如果当前号码是手机号码
				if (phoneNum.length() == 11 && !isExits) {
					ContactsInfo user = new ContactsInfo();
					user.setN(name.length() > 10 ? name.substring(0, 9) : name);
					user.setP(phoneNum);
					if ("#".equals(sortKey)) {
						if(!TextUtils.isEmpty(name)){
							sortKey = PinYin2Abbreviation.cn2py(name).substring(0, 1).toUpperCase();
						}else{
							sortKey = "#";
						}
						
						if (!isEnChar(sortKey)) {
							sortKey = "#";
						}
					}
					user.setSortKey(sortKey);
					listContacts.add(user);
				}
			}
		}
		cursor.close();
		return listContacts;
	}

	private static boolean isEnChar(String str) {
		String regex = ".*[a-zA-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(str);
		return m.matches();
	}

	/**
	 * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
	 *
	 * @param sortKeyString
	 *            数据库中读取出的sort key
	 * @return 英文字母或者#
	 */
	private static String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

	/**
	 * delete
	 * 
	 * @param name
	 *            linkman name
	 * @param context
	 *            context
	 * @return true success; false is fail
	 */
	public static boolean deleteLinkMan(String name, Context context) {
		if (name == null) {
			return false;
		}
		// 根据姓名求id
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { Data._ID }, "display_name=?", new String[] { name }, null);
		if (cursor.moveToFirst()) {
			int id = cursor.getInt(0);
			// 根据id删除data中的相应数据
			resolver.delete(uri, "display_name=?", new String[] { name });
			uri = Uri.parse("content://com.android.contacts/data");
			resolver.delete(uri, "raw_contact_id=?", new String[] { id + "" });
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * addLinkman
	 * 
	 * @param activity
	 * @return
	 */

	public static boolean insertLinkmanInfo(final Activity context, final String name, final String phone,
			final String email, final String address) {
		boolean isFlag = false;
		Uri muri = Uri.parse("content://com.android.contacts/data/phones/filter/" + name);
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(muri, new String[] { Data.DISPLAY_NAME }, null, null, null); // 从raw_contact表中返回display_name
		if (cursor.moveToFirst()) {
			isFlag = true;
		}
		cursor.close();
		if (isFlag) {
			return false;
		}
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		// 向raw_contact表添加一条记录
		ContentProviderOperation operation1 = ContentProviderOperation.newInsert(uri).withValue("account_name", null)
				.build();
		operations.add(operation1);
		// 向data添加数据
		uri = Uri.parse("content://com.android.contacts/data");
		// 添加姓名
		if (name == null || "".equals(name)) {
			ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
					.withValueBackReference("raw_contact_id", 0)
					// withValueBackReference的第二个参数表示引用operations[0]的操作的返回id作为此值
					.withValue("mimetype", "vnd.android.cursor.item/name").withValue("data2", phone).build();
			operations.add(operation2);
		} else {
			ContentProviderOperation operation2 = ContentProviderOperation.newInsert(uri)
					.withValueBackReference("raw_contact_id", 0)
					// withValueBackReference的第二个参数表示引用operations[0]的操作的返回id作为此值
					.withValue("mimetype", "vnd.android.cursor.item/name").withValue("data2", name).build();
			operations.add(operation2);
		}

		// 添加手机数据
		ContentProviderOperation operation3 = ContentProviderOperation.newInsert(uri)
				.withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/phone_v2")
				.withValue("data2", "2").withValue("data1", phone).build();
		operations.add(operation3);

		// 添加Email
		ContentProviderOperation operation4 = ContentProviderOperation.newInsert(uri)
				.withValueBackReference("raw_contact_id", 0).withValue("mimetype", "vnd.android.cursor.item/email_v2")
				.withValue("data2", "2").withValue("data1", email).build();
		operations.add(operation4);
		// 添加address
		ContentProviderOperation operation5 = ContentProviderOperation.newInsert(uri)
				.withValueBackReference("raw_contact_id", 0)
				.withValue("mimetype", "vnd.android.cursor.item/postal-address_v2").withValue("data2", "2")
				.withValue("data1", address).build();

		operations.add(operation5);
		try {
			resolver.applyBatch("com.android.contacts", operations);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * update
	 * 
	 * @param context
	 * @param name
	 * @param params
	 * @return success or faild
	 */
	public static boolean updateLinkmanInfo(Context context, String name, Map<String, String> params) {
		if (name == null || name.equals("") || params.size() == 0) {
			return false;
		}
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { Data._ID }, "display_name=?", new String[] { name }, null);
		if (cursor.moveToFirst()) {
			if (listdata == null) {
				listdata = new ArrayList<String>();
			} else {
				listdata.clear();
			}
			if (listInsertIndex == null) {
				listInsertIndex = new ArrayList<String>();
			} else {
				listInsertIndex.clear();
			}
			Param[] mParams = changeParams(params);
			for (int i = 0; i < params.size(); i++) {
				String index = getIndex(mParams[i].key);
				if (index != null) {
					listInsertIndex.add(index);
				} else {
					break;
				}
				listdata.add(mParams[i].value);
			}
			if (params.size() != listInsertIndex.size()) {
				return false;
			}
			int id = cursor.getInt(0);
			Uri uri2 = Uri.parse("content://com.android.contacts/data");// 对data表的所有数据操作
			ContentValues values = new ContentValues();

			for (int i = 0; i < listInsertIndex.size(); i++) {
				values.clear();
				values.put("data1", listdata.get(i));
				resolver.update(uri2, values, "mimetype=? and raw_contact_id=?",
						new String[] { listInsertIndex.get(i), id + "" });
			}
			cursor.close();
			return true;
		}
		return false;

	}

	/**
	 * 查询所有的通话记录
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<ContentValues> findCalllog(Context context) {
		if (queryList == null) {
			queryList = new ArrayList<ContentValues>();
		} else
			queryList.clear();

		ContentResolver resolver = context.getContentResolver();
		Uri uri = Calls.CONTENT_URI;
		String[] columns = new String[] { Calls._ID, // 0
				"name", // 1
				Calls.NUMBER, // 2
				Calls.TYPE, // 3
				Calls.DATE // 4
		};
		Cursor cursor = resolver.query(uri, columns, null, null, Calls.DATE + " desc");
		while (cursor.moveToNext()) {
			ContentValues values = new ContentValues();
			values.put(ID, cursor.getInt(0));
			values.put(NAME, cursor.getString(1));
			values.put(PHONE, cursor.getString(2));
			values.put(CALLTYPE, cursor.getInt(3) + "");
			values.put(CALLTIME, cursor.getLong(4) + "");
			queryList.add(values);
		}
		cursor.close();
		return queryList;
	}

	private static Param[] changeParams(Map<String, String> params) {
		Set<Entry<String, String>> entrySet = params.entrySet();
		int size = params.size();
		Param[] res = new Param[size];
		int i = 0;
		for (Map.Entry<String, String> entry : entrySet) {
			res[i++] = new Param(entry.getKey(), entry.getValue());
		}
		return res;
	}

	private static String getIndex(String data) {
		String Index = "";
		if (data.equals(NAME)) {
			Index = QUERYNAME;
		} else if (data.equals(EMAIL)) {
			Index = QUERYEMAIL;
		} else if (data.equals(PHONE)) {
			Index = QUERYPHONE;
		} else if (data.equals(ADDRESS)) {
			Index = QUERYADDRESS;
		} else {
			return null;
		}
		return Index;
	}

	/**
	 * 
	 * @author xxq
	 *
	 *         2015年12月15日
	 */
	public static class Param {
		public Param() {
		}

		public Param(String key, String value) {
			this.key = key;
			this.value = value;
		}

		String key;
		String value;
	}

	public static boolean checkPhoneNo(String phoneNum) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(phoneNum);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

}