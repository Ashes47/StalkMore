package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

        LinearLayout linLayout;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_feed);

            Intent intent = getIntent();
            String username = intent.getStringExtra("username");


            setTitle(username + "'s Photos");

            linLayout = findViewById(R.id.linLayout);

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Image");

            query.whereEqualTo("username", username);
            query.orderByDescending("createdAt");

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null && data != null) {
                                        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));
                                        imageView.setImageBitmap(bitmap);
                                        imageView.setPadding(16,16,16,16);
                                        linLayout.addView(imageView);
                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                try {

                                                Intent intent = new Intent(getApplicationContext(),FullScreenImageActivity.class);

                                                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bs);

                                                intent.putExtra("byteArray", bs.toByteArray());
                                                startActivity(intent);

                                                } catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            });
    }
}
