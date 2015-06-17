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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.locationbasedtrackingsystem.activity.R;

public class UpComingCtl extends BaseCtl {

	private ListAdapter adapter;
	private ListView view;
	private DataModel model;
	private String remove;
	private int ids;
	private ArrayList<DataModel> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_up_coming);
		final ListView view = (ListView) findViewById(R.id.listView_upa);
		// --------------- Delete Event On List View-------------------------//
		view.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter1, View view1,
					int position, long id) {
				ids = (int) id;

				model = (DataModel) view.getItemAtPosition(position);

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						UpComingCtl.this);
				// Setting Dialog Title
				alertDialog.setTitle("Confirm Delete...");
				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete "
						+ model.getTaskName() + "?");
				// Setting Icon to Dialog
				// alertDialog.setIcon(R.drawable.delete);
				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									model.delete();
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

		// -----------------Edit & View Event from Database----------------//
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter2, View view2,
					int position, long id) {
				ids = (int) id;
				model = (DataModel) view.getItemAtPosition(position);
				Intent intent = new Intent(getApplicationContext(),
						DailyRoutineCtl.class);
				intent.putExtra("TaskID", model.getId());
				intent.putExtra("TaskName", model.getTaskName());
				intent.putExtra("TaskDate", model.getTaskDate());
				intent.putExtra("TaskLocation", model.getTaskLocation());
				intent.putExtra("TaskRadius", model.getTaskRadius());
				intent.putExtra("TaskNearby", model.getTaskNearby());
				intent.putExtra("TaskTime", model.getTaskTime());
				startActivity(intent);
			}
		});

		// ------------------get Data from Database---------------------//
		model = new DataModel(getApplicationContext());

		list = model.getUpcomingTask();

		adapter = new ListAdapter(getApplicationContext(), R.layout.passedtask,
				list);

		view.setAdapter(adapter);
	}

	class ListAdapter extends ArrayAdapter {
		ArrayList<DataModel> passedTask;
		Context context;
		Object mLock = new Object();

		ArrayList<DataModel> completList = new ArrayList<DataModel>();

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<DataModel> list) {
			super(context, textViewResourceId);
			this.passedTask = list;
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
				return passedTask != null ? passedTask.size() : 0;
			}
		}

		@Override
		public DataModel getItem(int position) {
			DataModel dto;
			synchronized (mLock) {
				dto = passedTask != null ? passedTask.get(position) : null;
			}
			return dto;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.passedtask, null); // --CloneChangeRequired(list_item)
			}
			DataModel dto = null; // --CloneChangeRequired
			synchronized (mLock) {
				dto = (DataModel) passedTask.get(position);
			}
			if (dto != null) {
				TextView taskName = (TextView) view
						.findViewById(R.id.taskName_pt);

				TextView taskDate = (TextView) view
						.findViewById(R.id.taskDate_pa);
				TextView taskTime = (TextView) view
						.findViewById(R.id.taskTime_pa);

				taskName.setText(dto.getTaskName());
				taskDate.setText(dto.getTaskDate());
				taskTime.setText(dto.getTaskTime());
			}
			return view;
		}
	}
}