package in.co.sunrays.lbts.ctl;

import in.co.sunrays.lbts.model.DataModel;

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

import com.locationbasedtrackingsystem.activity.R;

public class FavoritePlaceCtl extends BaseCtl {

	private ListAdapter adapter;
	private ListView view;
	private int ids;
	private ArrayList<DataModel> list;
	private DataModel model;
	private Button add;

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
				model = (DataModel) view.getItemAtPosition(position);

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						FavoritePlaceCtl.this);

				// Setting Dialog Title
				alertDialog.setTitle("Confirm Delete...");

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete "
						+ model.getTaskLocation() + "?");
				// Setting Icon to Dialog
				// alertDialog.setIcon(R.drawable.delete);
				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								model = new DataModel(getApplicationContext());
								try {
									model.deleteLocation();
									// ----------automatic refresh the
									// page-----------//
									list.remove(ids);
									// adapter.remove(adapter.getItemId(ids));
									adapter.notifyDataSetChanged();
								} catch (Exception e) {
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
				ids = (int) id;
				model = (DataModel) view.getItemAtPosition(position);
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						FavoritePlaceCtl.this);
				// Setting Dialog Title
				alertDialog.setTitle("What you want....?");
				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want Edit or Add "
						+ model.getTaskLocation() + "?");
				// Setting Icon to Dialog
				// alertDialog.setIcon(R.drawable.delete);
				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("EDIT",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(
										getApplicationContext(),
										DemoMapCtl.class);
								intent.putExtra("TaskID", model.getId());
								intent.putExtra("TaskLocation",
										model.getTaskLocation());
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
										DailyRoutineCtl.class);
								intent.putExtra("TaskLocation",
										model.getTaskLocation());
								intent.putExtra("latitude", model.getLatitude());
								intent.putExtra("longitude",
										model.getLongitude());
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
				String loc = null;
				Intent intent = new Intent(getApplicationContext(),
						DemoMapCtl.class);
				intent.putExtra("TaskLocation", "loc");
				intent.putExtra("Task", loc);
				intent.putExtra("Check", loc);
				startActivity(intent);
			}
		});
		model = new DataModel(getApplicationContext());
		list = model.getLocation();
		adapter = new ListAdapter(getApplicationContext(), R.layout.list_view,
				list);
		view.setAdapter(adapter);
	}

	class ListAdapter extends ArrayAdapter {
		ArrayList<DataModel> locationList;
		Context context;
		Object mLock = new Object();

		ArrayList<DataModel> completList = new ArrayList<DataModel>();

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<DataModel> list) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
			this.locationList = list;
			this.context = context;
		}

		public void clone(ArrayList<DataModel> list) {
			for (Iterator<DataModel> it = list.iterator(); it.hasNext();) {
				DataModel dto = (DataModel) it.next();
				completList.add(dto);
			}
		}

		@Override
		public int getCount() {
			synchronized (mLock) {
				return locationList != null ? locationList.size() : 0;
			}
		}

		@Override
		public DataModel getItem(int position) {
			DataModel dto;
			synchronized (mLock) {
				dto = locationList != null ? locationList.get(position) : null;
			}
			return dto;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.list_view, null); // --CloneChangeRequired(list_item)
			}
			DataModel dto = null; // --CloneChangeRequired
			synchronized (mLock) {
				dto = (DataModel) locationList.get(position);
			}

			if (dto != null) {
				TextView Location = (TextView) view
						.findViewById(R.id.checkedTextView1);
				Location.setText(dto.getTaskLocation());
			}
			return view;
		}
	}
}