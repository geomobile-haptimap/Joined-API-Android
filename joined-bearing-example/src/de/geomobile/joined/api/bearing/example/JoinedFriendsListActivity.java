package de.geomobile.joined.api.bearing.example;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.geomobile.joined.api.bearing.JOBearingActivity;
import de.geomobile.joined.api.bearing.config.JOBearingConfig;
import de.geomobile.joined.api.bearing.helper.JOBearingBPOI;
import de.geomobile.joined.api.data.JOFriend;

public class JoinedFriendsListActivity extends Activity
{

	private List<JOFriend> friends;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		friends = JoinedManager.getInstance().getFriends();

		ListView friendsList = (ListView) findViewById(android.R.id.list);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

		for (JOFriend friend : friends)
		{
			adapter.add(friend.getNickname());
		}

		friendsList.setAdapter(adapter);

		friendsList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				arg1.showContextMenu();

			}
		});

		friendsList.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
		{
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
			{
				menu.add(0, 1, Menu.NONE, "Bearing");
				menu.add(0, 2, Menu.NONE, "Show on map");
			}
		});

	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Bundle bundle = new Bundle();
		JOBearingBPOI poi = new JOBearingBPOI(friends.get(info.position).getLatitude(), friends.get(info.position).getLongitude(), "");
		bundle.putSerializable(JOBearingConfig.DESTINATION, poi);

		switch (item.getItemId())
		{
		case 1:
		{
			Intent intent = new Intent(this, JOBearingActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		}

		case 2:
		{
			Intent intent = new Intent(this, JoinedMapActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		}

		}
		return super.onContextItemSelected(item);
	}
}