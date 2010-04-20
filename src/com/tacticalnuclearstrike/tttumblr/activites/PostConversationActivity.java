package com.tacticalnuclearstrike.tttumblr.activites;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tacticalnuclearstrike.tttumblr.R;
import com.tacticalnuclearstrike.tttumblr.TumblrApi;

public class PostConversationActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.postquoteview);

		setupOkButton();
	}
	
	private void returnToMainActivity() {
		setResult(RESULT_OK);
		finish();
	}

	private void setupOkButton() {
		Button btnOk = (Button) findViewById(R.id.postQuoteBtnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				okButtonClick();
			}
		});
	}

	private void okButtonClick() {
		EditText quote = (EditText) findViewById(R.id.inputQuote);
		EditText source = (EditText) findViewById(R.id.inputSource);
		
		final String quoteText = quote.getText().toString();
		final String sourceText = source.getText().toString();
		
		if(quoteText.compareTo("") == 0){
			Toast.makeText(this, "Cannont create post without content!", Toast.LENGTH_SHORT).show();
			return;		
		}
		
		final TumblrApi api = new TumblrApi(this);
		
		Toast.makeText(this, "Creating post", Toast.LENGTH_LONG).show();
		
		new Thread(new Runnable() {
			public void run() {
				api.postQuote(quoteText, sourceText);
			}
		}).start();
		
		returnToMainActivity();
	}
}
