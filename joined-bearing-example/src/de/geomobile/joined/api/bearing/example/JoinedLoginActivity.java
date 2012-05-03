package de.geomobile.joined.api.bearing.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class JoinedLoginActivity extends Activity
{

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

	public void onLoginClick(View view)
	{
		EditText nicknameEditText = (EditText) findViewById(R.id.nickname);
		EditText passwordEditText = (EditText) findViewById(R.id.password);

		String nickname = nicknameEditText.getText().toString();
		String password = passwordEditText.getText().toString();

		if (JoinedManager.getInstance().login(nickname, password))
		{
			startActivity(new Intent(this, JoinedFriendsListActivity.class));
		}
		else
		{
			Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
		}
	}

}
