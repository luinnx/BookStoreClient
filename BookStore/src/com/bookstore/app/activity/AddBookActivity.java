package com.bookstore.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bookstore.app.base.BookStoreActionBarBase;

public class AddBookActivity extends BookStoreActionBarBase {

	Spinner spBookTypes, spBooksTypesElements, spTextBooksElements,
			spThirdDegreeElements;
	String[] bookTypes, guideSubElements, textBookSubElements,
			thirdDergeeSubElements;
	String bookType, secondDegreeSubElement;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book_test);
		initViews();
	}

	private void initViews() {
		spBookTypes = (Spinner) findViewById(R.id.spBookTypes);
		spBooksTypesElements = (Spinner) findViewById(R.id.spBooksTypesElements);
		spThirdDegreeElements = (Spinner) findViewById(R.id.spThirdDegreeElements);
		bookTypes = new String[] { "Guide Books", "Text Books" };
		guideSubElements = new String[] { "SSC", "HSC", "DEGREE", "HONOURS",
				"MASTERS", "TEST PAPERS" };
		textBookSubElements = new String[] { "DEGREE", "HONOURS" };
		thirdDergeeSubElements = new String[] { "1st Year", "2nd Year",
				"3rd Year" };
		spBookTypes.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, bookTypes));

		spBookTypes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				bookType = spBookTypes.getSelectedItem().toString();
				spBooksTypesElements.setVisibility(View.VISIBLE);
				if (bookType.equals("Guide Books")) {
					spBooksTypesElements.setAdapter(new ArrayAdapter<String>(
							AddBookActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							guideSubElements));
					spBooksTypesElements
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									secondDegreeSubElement = spBooksTypesElements
											.getSelectedItem().toString();

									if (secondDegreeSubElement
											.equals("HONOURS")) {
										spThirdDegreeElements
												.setVisibility(View.VISIBLE);
										spThirdDegreeElements
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
									}else{
										spThirdDegreeElements
										.setVisibility(View.GONE);
									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {

								}
							});
				} else {
					spBooksTypesElements.setAdapter(new ArrayAdapter<String>(
							AddBookActivity.this,
							android.R.layout.simple_dropdown_item_1line,
							textBookSubElements));
					spBooksTypesElements
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									secondDegreeSubElement = spBooksTypesElements
											.getSelectedItem().toString();
									if (secondDegreeSubElement
											.equals("HONOURS")) {
										spThirdDegreeElements
												.setVisibility(View.VISIBLE);
										thirdDergeeSubElements = new String[] {
												"1st Year", "2nd Year",
												"3rd Year" };
										spThirdDegreeElements
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
									} else {
										spThirdDegreeElements
												.setVisibility(View.VISIBLE);
										thirdDergeeSubElements = new String[] { "1st Year" };
										spThirdDegreeElements
												.setAdapter(new ArrayAdapter<String>(
														AddBookActivity.this,
														android.R.layout.simple_dropdown_item_1line,
														thirdDergeeSubElements));
									}

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}
							});
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

}
