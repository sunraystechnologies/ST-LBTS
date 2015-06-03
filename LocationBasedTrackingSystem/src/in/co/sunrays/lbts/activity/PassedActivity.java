package in.co.sunrays.lbts.activity;

import in.co.sunrays.lbts.dao.DataDAO;
import in.co.sunrays.lbts.dto.DataDTO;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.locationbasedtrackingsystem.activity.BaseActivity;
import com.locationbasedtrackingsystem.activity.R;

public class PassedActivity extends BaseActivity {

	ListAdapter adapter;
	ListView view;
	DataDTO dto;
	DataDAO dao;
	String remove;
	int ids;
	ArrayList<DataDTO> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passed);

		final ListView view = (ListView) findViewById(R.id.listView_pta);

		// ---------------Event On List View-------------------------//
		view.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter1, View view1,
					int position, long id) {

				ids = (int) id;
				// remove= adapter1.getItemAtPosition(position).toString();

				dto = (DataDTO) view.getItemAtPosition(position);

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						PassedActivity.this);

				// Setting Dialog Title
				alertDialog.setTitle("Confirm Delete...");

				// Setting Dialog Message
				alertDialog.setMessage("Are you sure you want delete "
						+ dto.getTaskName() + "?");

				// Setting Icon to Dialog
				// alertDialog.setIcon(R.drawable.delete);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								dao = new DataDAO(getApplicationContext());
								try {
									dao.delete(dto);

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

		// ------------------get Data from Database---------------------//
		dao = new DataDAO(getApplicationContext());

		list = dao.getPassedTask();

		adapter = new ListAdapter(getApplicationContext(), R.layout.passedtask,
				list);

		view.setAdapter(adapter);
	}

	class ListAdapter extends ArrayAdapter {

		ArrayList<DataDTO> passedTask;
		Context context;
		Object mLock = new Object();

		ArrayList<DataDTO> completList = new ArrayList<DataDTO>();

		public ListAdapter(Context context, int textViewResourceId,
				ArrayList<DataDTO> list) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
			this.passedTask = list;
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
				return passedTask != null ? passedTask.size() : 0;
			}
		}

		@Override
		public DataDTO getItem(int position) {
			// TODO Auto-generated method stub
			DataDTO dto;
			synchronized (mLock) {
				dto = passedTask != null ? passedTask.get(position) : null;
			}
			return dto;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;

			if (view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.passedtask, null); // --CloneChangeRequired(list_item)
			}

			DataDTO dto = null; // --CloneChangeRequired
			synchronized (mLock) {
				dto = (DataDTO) passedTask.get(position);
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

				// id.setText(dto.getId());

			}
			return view;
		}
	}

}
