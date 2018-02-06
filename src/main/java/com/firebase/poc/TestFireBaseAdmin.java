package com.firebase.poc;

import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestFireBaseAdmin {

	private String fireBaseDbConnexion = "src/main/resources/fireBaseGoogleCerts.json";

	private String firebaseDbUrl = "https://.firebaseio.com";

	@SuppressWarnings("unused")
	private String projectId = "projectId";

	@SuppressWarnings("unused")
	private String fireBaseBucket = "fireBaseBucket.appspot.com";

	@Test
	public void saveFirebase() throws Exception {
		final ObjectToInsertDto dto = new ObjectToInsertDto();
		String dataPath = "/data/table_firebase";

		final FileInputStream serviceAccount = new FileInputStream(fireBaseDbConnexion);

		final FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount)).setDatabaseUrl(firebaseDbUrl).build();

		if (FirebaseApp.getApps().size() == 0) {
			// Firebase not initialized automatically, do it manually
			FirebaseApp.initializeApp(options);
		}
		


		final DatabaseReference dataTable = FirebaseDatabase.getInstance().getReference().child(dataPath);

		dataTable.orderByKey().addValueEventListener(new ValueEventListener() {

			@SuppressWarnings("unchecked")
			public void onDataChange(DataSnapshot dataSnapshot) {
				final List<Object> dataValue = (List<Object>) dataSnapshot.getValue();
				dataValue.add(dto);

				dataTable.setValue(dataValue, new DatabaseReference.CompletionListener() {
					public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
					}
				});
				dataTable.removeEventListener(this);
			}

			public void onCancelled(DatabaseError dataSnapshot) {
				dataTable.removeEventListener(this);
			}
		});
	}
}
