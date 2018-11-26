package com.viztushar.stickers;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.viztushar.stickers.adapter.StickerAdapter;
import com.viztushar.stickers.model.StickerModel;
import com.viztushar.stickers.task.GetStickers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetStickers.Callbacks {

    private static final int ADD_PACK = 200;
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    private static final String TAG = MainActivity.class.getSimpleName();
    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    static String path;
    ArrayList<String> strings;
    StickerAdapter adapter;
    ArrayList<StickerPack> stickerPacks = new ArrayList<>();
    List<Sticker> mStickers;
    ArrayList<StickerModel> stickerModels = new ArrayList<>();
    RecyclerView recyclerView;
    List<String> mEmojis;
    String android_play_store_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stickerPacks = new ArrayList<>();
        path = getFilesDir() + "/" + "stickers_asset" + "/" + "100045";
        stickerPacks.add(new StickerPack("100045", "name",
                "tushar", "0.png", "", "", "", ""));
        mStickers = new ArrayList<>();
        stickerModels = new ArrayList<>();
        mEmojis = new ArrayList<>();
        mEmojis.add("");
        adapter = new StickerAdapter(this, stickerModels);
        getPermissions();
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + Hawk.get("sticker_pack", new ArrayList<StickerPack>()));
                Intent intent = new Intent();
                intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
                intent.putExtra(EXTRA_STICKER_PACK_ID, "100045");
                intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
                intent.putExtra(EXTRA_STICKER_PACK_NAME, "name");
                try {
                    startActivityForResult(intent, ADD_PACK);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
                }
            }
        });

        new GetStickers(this, this, getResources().getString(R.string.json_link)).execute();
    }


    public static void SaveImage(Bitmap finalBitmap, String name) {

        String root = path;
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = name + ".webp";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.WEBP, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SaveTryImage(Bitmap finalBitmap, String name) {

        String root = path;
        File myDir = new File(root + "/" + "try");
        myDir.mkdirs();
        String fname = name + ".png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPermissions() {
        int perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perm != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    1
            );
        }
    }

    @Override
    public void onListLoaded(String jsonResult, boolean jsonSwitch) {
        try {
            if (jsonResult != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    android_play_store_link = jsonResponse.getString("android_play_store_link");
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("sticker_packs");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        stickerPacks.add(new StickerPack(
                                jsonChildNode.getString("identifier"),
                                jsonChildNode.getString("name"),
                                jsonChildNode.getString("publisher"),
                                getLastBitFromUrl(jsonChildNode.getString("tray_image_file")),
                                jsonChildNode.getString("publisher_email"),
                                jsonChildNode.getString("publisher_website"),
                                jsonChildNode.getString("privacy_policy_website"),
                                jsonChildNode.getString("license_agreement_website")
                        ));
                        JSONArray stickers = jsonChildNode.getJSONArray("stickers");
                        for (int j = 0; j < stickers.length(); j++) {
                            JSONObject jsonStickersChildNode = stickers.getJSONObject(j);
                                /*JSONArray emojis = jsonStickersChildNode.getJSONArray("emojis");
                                for (int k = 0; k < emojis.length(); k++) {
                                    JSONObject jsonemojisChildNode = jsonMainNode.getJSONObject(k);
                                    mEmojis.add(jsonemojisChildNode.ge)
                                }*/
                            Log.d(TAG, "onListLoaded: " + jsonStickersChildNode.getString("image_file"));
                            mStickers.add(new Sticker(
                                    getLastBitFromUrl(jsonStickersChildNode.getString("image_file")),
                                    mEmojis
                            ));
                        }
                        stickerPacks.get(i).setStickers(mStickers);
                        stickerModels.add(new StickerModel(
                                jsonChildNode.getString("name"),
                                mStickers.get(0).imageFileName,
                                mStickers.get(1).imageFileName,
                                mStickers.get(2).imageFileName,
                                mStickers.get(2).imageFileName
                        ));
                    }
                    Hawk.put("sticker_packs", stickerPacks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new StickerAdapter(this, stickerModels);
                recyclerView.setAdapter(adapter);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onListLoaded: " + stickerModels.size());
    }

    private static String getLastBitFromUrl(final String url) {
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }
}
