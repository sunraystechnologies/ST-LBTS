package in.co.sunrays.lbts.activity;

import in.co.sunrays.lbts.dao.DataDAO;
import in.co.sunrays.lbts.dto.DataDTO;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.locationbasedtrackingsystem.activity.BaseActivity;
import com.locationbasedtrackingsystem.activity.R;

public class FavoritePlaceActivity extends BaseActivity {

	ListAdapter adapter;
	ListView view;
	int ids;
	ArrayList<DataDTO> list;
	DataDTO dto;
	DataDAO dao;
	Button add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_place);

		Toast.makeText(getApplicationContext(),
				"Welcome to Favorite Place List", Toast.LENGTH_LONG).show();
		view = (ListView) findViewById(R.id.listId);

		// --------------------Delete Event from Favourite List---------------//

		view.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter1, View view1,
					int position, long id) {

				ids = (int) id;
				System.out.println("idssssssss" + ids);
				dto = (DataDTO) view.getItemAtPosition(position);

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						FavoritePlaceActivity.this);

				// Setting Dialog Title
				alertDialog.setTitle("Confirm Delete...");

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete "
						+ dto.getTaskLocation() + "?");

				// Setting Icon to Dialog
				// alertDialog.setIcon(R.drawable.delete);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dao = new DataDAO(getApplicationContext());
								try {
									dao.deleteLocation(dto);
									// ----------automatic refresh the
									// page-----------//
									list.remove(ids);
									// adapter.remove(adapter.getItemId(ids));
									adapter.notifyDataSetChanged();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to invoke NO event
								Toast.makeText(getApplicationContext(),
										"You clicked on NO", Toast.LENGTH_SHORT)
										.show();
								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();

				return true;
			}

		});

		// -------------------Edit & ADD Event-------------------------//
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter2, View view2,
					int position, long id) {
				// TODO Auto-generated method stub

				ids = (int) id;
				dto = (DataDTO) view.getItemAtPosition(position);
				// System.out.println("idsssssssssssss"+id);
				System.out.println("Favourite Place Activity"
						+ dto.getTaskRadius());

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						FavoritePlaceActivity.this);

				// Setting Dialog Title
				alertDialog.setTitle("What you want....?");

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want Edit or Add "
						+ dto.getTaskLocation() + "?");

				// Setting Icon to Dialog
				// alertDialog.setIcon(R.drawable.delete);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("EDIT",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(
										getApplicationContext(),
										DemoMapActivity.class);
								intent.putExtra("TaskID", dto.getID());
								intent.putExtra("TaskLocation",
										dto.getTaskLocation());
								intent.putExtra("Check", "check");
								startActivity(intent);

							}
						});
				// Setting Negative "ADD" Button
				alertDialog.setNegativeButton("ADD",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								Intent intent = new Intent(
										getApplicationContext(),
										DailyRoutineActivity.class);
								System.out.println("ADD in the Daily Routine"
										+ dto.getTaskLocation());
								intent.putExtra("TaskLocation",
										dto.getTaskLocation());
								intent.putExtra("latitude", dto.getLatitude());
								intent.putExtra("longitude", dto.getLongitude());
								setResult(RESULT_OK, intent);
								finish();

							}
						});

				// Showing Alert Message
				alertDialog.show();

			}
		});

		// -------------------ADD Button Function---------------//
		add = (Button) findViewById(R.id.add_fpa);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String loc = null;
				Intent intent = new Intent(getApplicationContext(),
						DemoMapActivity.class);
				intent.putExtra("TaskLocation", "loc");
				intent.putExtra("Task", loc);
				intent.putExtra("Check", loc);
				startActivity(intent);
			}
		});

		dao = new DataDAO(getApplicationContext());

		list = dao.getLocation();

		adapter = new ListAdapter(getApplicationContext(), R.layout.list_view,
				list);

		view.setAdapter(adapter);

	}

	class ListAdapter extends ArrayAdapter {

		ArrayList<DataDTO> locationList;
		Context context;
		Object mLock = new Object();

		ArrayList<DataDTO> completList = new ArrayList<DataDTO>();

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<DataDTO> list) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
			this.locationList = list;
			this.context = context;
		}

		public void clone(ArrayList<DataDTO> list) {
			for (Iterator<DataDTO> it = list.iterator(); it.hasNext();) {
				DataDTO dto = (DataDTO) it.next();
				completList.add(dto);

			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			synchronized (mLock) {
				return locationList != null ? locationList.size() : 0;
			}
		}

		@Override
		public DataDTO getItem(int position) {
			// TODO Auto-generated method stub
			DataDTO dto;
			synchronized (mLock) {
				dto = locationList != null ? locationList.get(position) : null;
			}
			return dto;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;

			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.list_view, null); // --CloneChangeRequired(list_item)
			}

			DataDTO dto = null; // --CloneChangeRequired
			synchronized (mLock) {
				dto = (DataDTO) locationList.get(position);
			}

			if (dto != null) {

				TextView Location = (TextView) view
						.findViewById(R.id.checkedTextView1);

				Location.setText(dto.getTaskLocation());

				// id.setText(dto.getId());

			}
			return view;
		}

	}

}
