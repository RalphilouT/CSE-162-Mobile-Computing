package com.ralphiloutatoy.lab6facedetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


//import org.bytedeco.javacv.AndroidFrameConverter;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.bytedeco.javacv.OpenCVFrameConverter;
//import org.bytedeco.opencv.opencv_core.Mat;




public class MainActivity extends AppCompatActivity {
    ImageView iw;
    Canvas canvas;
    Bitmap mutableBitmap;
    String file = "video.mp4";
    String dir = "C:\\Users\\ralph\\AndroidStudioProjects\\Lab6FaceDetector\\app\\src\\main\\assets";

    /*


     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FaceDetectorOptions highAccuracyOpts = new FaceDetectorOptions.Builder().setPerformanceMode(
                FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE).setLandmarkMode(
                        FaceDetectorOptions.LANDMARK_MODE_ALL).setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL).build();
//        InputStream inputStream = null;
//        try {
//            inputStream= new FileInputStream(dir+"/video.mp4");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputStream);
//        AndroidFrameConverter convertBitmap = new AndroidFrameConverter();
//        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
//        try {
//            grabber.start();
//        } catch (FFmpegFrameGrabber.Exception e) {
//            e.printStackTrace();
//        }
//        Mat img= new Mat();
//        Bitmap[] bm = null;
//        for(int frames = 0; frames < grabber.getLengthInVideoFrames();frames++) {
//            Frame nthFrame = null;
//            try {
//                nthFrame = grabber.grabImage();
//            } catch (FFmpegFrameGrabber.Exception e) {
//                e.printStackTrace();
//            }
//            bm[frames] = convertBitmap.convert(nthFrame);
//
//
//        }
//        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
//        try{
//            mmr.setDataSource("C:\\Users\\ralph\\AndroidStudioProjects\\Lab6FaceDetector\\app\\src\\main\\assets\\video.mp4");
//
//        }catch(Exception e){
//            System.out.println("Exception= "+e);
//        }
//        long duration = mmr.getMetadata().getLong(("duration"));
//        double frameRate = mmr.getMetadata().getDouble("framerate");
//        int numberOfFrame = (int) (duration/frameRate);
        //Bitmap bm= mmr.getFrameAtTime(1* 100000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
//        for(int i = 0; i<numberOfFrame;i++){
//            Bitmap bmp= mmr.getFrameAtTime(i* 100000L, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
//        }
//        File videoFile=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/assets/","vido.mp4");
//        Uri videoFileUri=Uri.parse(videoFile.toString());
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(videoFile.getAbsolutePath());
//        ArrayList<Bitmap> rev=new ArrayList<Bitmap>();
//        //Create a new Media Player
//        MediaPlayer mp = MediaPlayer.create(getBaseContext(), videoFileUri);
//        int millis = mp.getDuration();
//        for(int i=1000000;i<millis*1000;i+=1000000)
//        {
//            Bitmap bitmap=retriever.getFrameAtTime(i,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//            rev.add(bitmap);
//        }


        Bitmap bm = getBitmapFromAssets("faces.png");
        iw = (ImageView) findViewById(R.id.imageView);
        iw.setImageBitmap(bm);
        mutableBitmap = bm.copy(Bitmap.Config.ARGB_8888,true);
        canvas = new Canvas(mutableBitmap);
        InputImage image = InputImage.fromBitmap(bm,0);
        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
        Task<List<Face>> result = detector.process(image).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(List<Face> faces) {

                for(Face face :faces){
                    //Success at identifying the face
                    Rect bounds = face.getBoundingBox();
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);
                    paint.setColor(Color.YELLOW);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(8);
                    canvas.drawRect(bounds,paint);
                    iw = (ImageView) findViewById(R.id.imageView);
                    iw.setImageBitmap(mutableBitmap);
                    Log.d("TAG","recognition succeed");
                }
            }
        }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Task failed with an exception
                        Log.d("TAG", "recognition failed");
                        Toast.makeText(getApplicationContext(),(String)e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Bitmap getBitmapFromAssets(String fileName){
        AssetManager am = getAssets();
        InputStream is = null;
        try{
            is = am.open(fileName);
        }catch(IOException e){
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(is);

        return bitmap;
        /*

        view.setDrawingCacheEnabled(true);
        Bitmap b = view.getDrawingCache();
        b.compress(CompressFormat.JPEG, 95, new FileOutputStream("/some/location/image.jpg"));
         */
    }
    public void VideoToGif(String uri) {
//        Uri videoFileUri = Uri.parse(uri);
//
//        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
//        retriever.setDataSource(uri);
//        try{
//            retriever.setDataSource(uri);
//        }

    }
}