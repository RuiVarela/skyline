package com.skyline;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class BoardingData extends Activity  implements OnItemSelectedListener {
	public final static String BARCODE_STRING = "com.skyline.BARCODE_STRING";
	public final static String BARCODE_FORMAT = "com.skyline.BARCODE_FORMAT";
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boarding_data);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.iata_airport_codes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) findViewById(R.id.spinner_from);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		adapter = ArrayAdapter.createFromResource(this, R.array.iata_airport_codes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (Spinner) findViewById(R.id.spinner_to);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		adapter = ArrayAdapter.createFromResource(this, R.array.iata_carrier, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (Spinner) findViewById(R.id.spinner_carrier);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		adapter = ArrayAdapter.createFromResource(this, R.array.iata_format, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner = (Spinner) findViewById(R.id.spinner_format);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		SharedPreferences settings = getPreferences(0);
		String boarding_pass_stored = settings.getString("boarding_pass", "");

		if ((boarding_pass_stored != null) && (!boarding_pass_stored.equals(""))) {
			BoardingPass boarding_pass = new BoardingPass();
			boarding_pass.fromString(boarding_pass_stored);
			ApplyBoardingPassToGUI(boarding_pass);
		}

		EditText edit = (EditText) findViewById(R.id.edit_gate_key);
		edit.setText("!VBKEY01");
	}
    
	@Override
	protected void onStop() {
		super.onStop();

		BoardingPass boarding_pass = GenerateBoardingPassFromGUI();

		SharedPreferences settings = getPreferences(0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("boarding_pass", boarding_pass.toString());
		editor.commit();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_boarding_data, menu);
        return true;
    }
    
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	public void ApplyBoardingPassToGUI(BoardingPass boarding_pass) {
		EditText edit = null;
		Spinner spinner = null;

		edit = (EditText) findViewById(R.id.edit_passenger_name);
		edit.setText(boarding_pass.PassengerName);

		edit = (EditText) findViewById(R.id.edit_flight_number);
		edit.setText(boarding_pass.FlightNumber);	
    	
		spinner = (Spinner) findViewById(R.id.spinner_from);
		for (int i = 0; i != spinner.getCount(); ++i) {
			if (spinner.getItemAtPosition(i).toString()
					.equals(boarding_pass.FromCityAirportCode)) {
				spinner.setSelection(i);
				break;
			}
		}
		
		spinner = (Spinner) findViewById(R.id.spinner_to);
		for (int i = 0; i != spinner.getCount(); ++i) {
			if (spinner.getItemAtPosition(i).toString()
					.equals(boarding_pass.ToCityAirportCode)) {
				spinner.setSelection(i);
				break;
			}
		}
		
		spinner = (Spinner) findViewById(R.id.spinner_carrier);
		for (int i = 0; i != spinner.getCount(); ++i) {
			if (spinner.getItemAtPosition(i).toString()
					.equals(boarding_pass.OperatingCarrierDesignator)) {
				spinner.setSelection(i);
				break;
			}
		}
		
		int julianDate = Integer.parseInt(boarding_pass.DateOfFlightJulian);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.DAY_OF_YEAR, julianDate);
		
		int month = cal.get(Calendar.MONTH);
		int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
		
		DatePicker date_picker = (DatePicker) findViewById(R.id.date_picker);
		date_picker.updateDate(year, month, day_of_month);
    }
    
    public BoardingPass GenerateBoardingPassFromGUI()
    {
    	EditText edit = null;
    	Spinner spinner =  null;
    	
    	BoardingPass boarding_pass = new BoardingPass();
    	
    	edit = (EditText) findViewById(R.id.edit_passenger_name);
    	boarding_pass.PassengerName = edit.getText().toString();
    	
    	edit = (EditText) findViewById(R.id.edit_flight_number);
    	boarding_pass.FlightNumber = edit.getText().toString();
    	if (boarding_pass.FlightNumber.equals("")) boarding_pass.FlightNumber = "0";
    	boarding_pass.FlightNumber = String.format("%04d", Integer.parseInt(boarding_pass.FlightNumber));
    	
		spinner = (Spinner) findViewById(R.id.spinner_from);
		boarding_pass.FromCityAirportCode = spinner.getSelectedItem().toString();
    	
		spinner = (Spinner) findViewById(R.id.spinner_to);
		boarding_pass.ToCityAirportCode = spinner.getSelectedItem().toString();
    	
		spinner = (Spinner) findViewById(R.id.spinner_carrier);
		boarding_pass.OperatingCarrierDesignator = spinner.getSelectedItem().toString();
		
		DatePicker date_picker = (DatePicker) findViewById(R.id.date_picker);
		int month = date_picker.getMonth();
		int day_of_month = date_picker.getDayOfMonth();
		int year = date_picker.getYear();
		
		Calendar cal = Calendar.getInstance();
		cal.set(GregorianCalendar.DAY_OF_MONTH, day_of_month);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.YEAR, year);
        boarding_pass.DateOfFlightJulian = String.format("%03d",cal.get(GregorianCalendar.DAY_OF_YEAR)); 
        
		return boarding_pass;
    }
    
    public void generate(View view) {
    	BoardingPass boarding_pass = GenerateBoardingPassFromGUI();
    	
    	Spinner spinner = (Spinner) findViewById(R.id.spinner_format);
		String format = spinner.getSelectedItem().toString();
		
    	Intent intent = new Intent(this, DisplayBarcode.class);
    	intent.putExtra(BARCODE_STRING, boarding_pass.toString());
    	intent.putExtra(BARCODE_FORMAT, format);
    	startActivity(intent);
    }
    
    public void gateEnable(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.spinner_format);
		String format = spinner.getSelectedItem().toString();
		
    	
    	EditText edit = (EditText) findViewById(R.id.edit_gate_key);
    	String key = edit.getText().toString();
    	String command = String.format("a super special code|%s", key);
    	Intent intent = new Intent(this, DisplayBarcode.class);
    	intent.putExtra(BARCODE_STRING, command);
    	intent.putExtra(BARCODE_FORMAT, format);
    	startActivity(intent);
    }
    
    public void gateDisable(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.spinner_format);
		String format = spinner.getSelectedItem().toString();
		
    	EditText edit = (EditText) findViewById(R.id.edit_gate_key);
    	String key = edit.getText().toString();
    	String command = String.format("a super special code|%s", key);
    	Intent intent = new Intent(this, DisplayBarcode.class);
    	intent.putExtra(BARCODE_STRING, command);
    	intent.putExtra(BARCODE_FORMAT, format);
    	startActivity(intent);
    }
    
    public void gateToggle(View view) {
    	Spinner spinner = (Spinner) findViewById(R.id.spinner_format);
		String format = spinner.getSelectedItem().toString();
		
    	EditText edit = (EditText) findViewById(R.id.edit_gate_key);
    	String key = edit.getText().toString();
    	String command = String.format("a super special code|%s", key);
    	Intent intent = new Intent(this, DisplayBarcode.class);
    	intent.putExtra(BARCODE_STRING, command);
    	intent.putExtra(BARCODE_FORMAT, format);
    	startActivity(intent);
    }
    
}
