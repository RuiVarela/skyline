package com.skyline;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class DisplayBarcode extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String data = intent.getStringExtra(BoardingData.BARCODE_STRING);
		String mode = intent.getStringExtra(BoardingData.BARCODE_FORMAT);
		Bitmap bitmap = CreateBarcodeImage(data, mode);
		ImageView view = new ImageView(this);
		view.setImageBitmap(bitmap);
		setContentView(view);
	}

	public Bitmap CreateBarcodeImage(String data, String mode) {
		Bitmap bitmap = null;
		BitMatrix matrix = null;
		
		com.google.zxing.BarcodeFormat format = com.google.zxing.BarcodeFormat.QR_CODE;
		
		if (mode.equals("QR_CODE")) {
			format = com.google.zxing.BarcodeFormat.QR_CODE;
		} else if (mode.equals("CODE_128")) {
			format = com.google.zxing.BarcodeFormat.CODE_128;
		} else if (mode.equals("PDF_417")) {
			format = com.google.zxing.BarcodeFormat.PDF_417;
		}
		
		int width = 512;
		int height = 512;
		com.google.zxing.Writer writer = new MultiFormatWriter();
		
		try {
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			//hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			//hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			matrix = writer.encode(data, format, width, height, hints);
		} catch (com.google.zxing.WriterException e) {
			System.out.println(e.getMessage());
			matrix = null;
		}
		
		if (matrix != null) {
			
			width = matrix.getWidth();
			height = matrix.getHeight();
			
			final int BLACK = 0xFF000000;
			final int WHITE = 0xFFFFFFFF;

			final int onColor = BLACK;
			final int offColor = WHITE;

			try {

				bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			} 
			catch (Exception ex){
				bitmap = null;
			}


			if (bitmap != null) {
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						bitmap.setPixel(x, y, matrix.get(x, y) ? onColor : offColor);
					}
				}
			}
		}	
		
		return bitmap;
	}
}