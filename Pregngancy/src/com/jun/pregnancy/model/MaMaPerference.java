package com.jun.pregnancy.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MaMaPerference {
	
	// For debugging
	private static final String TAG = "MamaPreferences";
	
	// Preferences name
	private static final String mama_PREFERENCE = "mama_preference";

	// Key user agreement of pregnancy plan
	private static final String KEY_AGREEMENT = "mama_agreement";
	
	/** key for lastComingDate */
	private static final String KEY_LASTCOMINGDATE = "last_date";
	
	/** key for estimating period of menstruation */
	private static final String KEY_PERIOD_MENSTRUATION = "period_menstruation";
	
	/** key for estimating cycles of menstruation */
	private static final String KEY_CYCLES_MENSTRUATION = "cycle_menstruation";
	
	/** Key for ages */
	private static final String KEY_AGES = "age";
	
	/** Key for first user*/
	private static final String KEY_FIRSTUSE = "firstUse";
	
	/** Instance */
	private static MaMaPerference m_Instance;  

	/** Context application */
	private Context m_Context;

	private MaMaPerference(Context context) {
		this.m_Context = context;
	}

	public synchronized static MaMaPerference getInstance(Context context) {
		if (m_Instance == null) {
			
			m_Instance = new MaMaPerference(context);
		}

		return m_Instance;
	}

	public void setFirstUse(boolean bIsFirst){
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor settingEditor = preferences.edit();
		settingEditor.putBoolean(KEY_FIRSTUSE, bIsFirst);
		settingEditor.commit();
		
	}
	
	public boolean getFirstUse() {
		/* Get value of setting from preference */
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);

		return preferences.getBoolean(KEY_FIRSTUSE, false);
	}
	
	
	public void setAccpetAgreement(boolean bIsAccept) {
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor settingEditor = preferences.edit();
		settingEditor.putBoolean(KEY_AGREEMENT, bIsAccept);
		settingEditor.commit();
	}

	public boolean getAcceptAgreement() {
		/* Get value of setting from preference */
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);

		return preferences.getBoolean(KEY_AGREEMENT, false);
	}

	public void setLastComingDate(long nTime) {
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor settingEditor = preferences.edit();
		settingEditor.putLong(KEY_LASTCOMINGDATE, nTime);
		settingEditor.commit();
	}

	public long getLastComingDate() {
		/* Get value of setting from preference */
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);

		return preferences.getLong(KEY_LASTCOMINGDATE, System.currentTimeMillis());
	}

	public void setPeriodMenstruation(int days) {
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor settingEditor = preferences.edit();
		settingEditor.putInt(KEY_PERIOD_MENSTRUATION, days);
		settingEditor.commit();
	}

	public int getPeriodMenstruation() {
		/* Get value of setting from preference */
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);

		return preferences.getInt(KEY_PERIOD_MENSTRUATION, 5);
	}

	
	public void setCycleMenstruation(int distances) {
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor settingEditor = preferences.edit();
		settingEditor.putInt(KEY_CYCLES_MENSTRUATION, distances);
		settingEditor.commit();
	}

	public int getCycleMenstruation() {
		/* Get value of setting from preference */
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);

		return preferences.getInt(KEY_CYCLES_MENSTRUATION, 28);
	}
	
	public void setAge(int age) {
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor settingEditor = preferences.edit();
		settingEditor.putInt(KEY_AGES, age);
		settingEditor.commit();
	}

	public int getAge() {
		/* Get value of setting from preference */
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);

		return preferences.getInt(KEY_AGES, 22);
	}
	

	private String _getStringPreferences(String strKey, String strDefaultValue) {
		/* Verify input */
		if (strKey == null) {
			Log.e(TAG, "Invalid input parameter");
			return strDefaultValue;
		}

		/* Get value of setting from preference */
		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);

		return preferences.getString(strKey, strDefaultValue);
	}

	private boolean _setStringPreferences(String strKey, String strValue) {
		/* Verify input parameter */
		if ((strKey == null) || (strValue == null)) {
			Log.e(TAG, "Invalid input parameter");
			return false;
		}

		SharedPreferences preferences = m_Context.getSharedPreferences(
				mama_PREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor settingEditor = preferences.edit();
		settingEditor.putString(strKey, strValue);
		boolean bResult = settingEditor.commit();

		return bResult;
	}
}
