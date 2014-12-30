package com.ilbreebchi.facedetection;

//Captures the camera stream with OpenCV  
//Search for the faces  
//* Display a circle around the faces using Java  
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Date;

import javax.swing.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class My_Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage image;

	// Create a constructor method
	public My_Panel() {
		super();
	}

	// * Converts/writes a Mat into a BufferedImage.
	// *
	// * @param matrix Mat of type CV_8UC3 or CV_8UC1
	// @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY

	public boolean MatToBufferedImage(Mat matBGR) {
		long startTime = System.nanoTime();
		int width = matBGR.width(), height = matBGR.height(), channels = matBGR
				.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		matBGR.get(0, 0, sourcePixels);
		// create new image and get reference to backing data
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
		long endTime = System.nanoTime();
		System.out.println(String.format("Elapsed time: %.2f ms",
				(float) (endTime - startTime) / 1000000));
		return true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.image == null)
			return;
		g.drawImage(this.image, 10, 10, this.image.getWidth(),
				this.image.getHeight(), null);
		// g.drawString("This is my custom Panel!",10,20);
	}
}

class processor {
	private CascadeClassifier fullbody;
	private CascadeClassifier lowerbody;
	private CascadeClassifier upperbody;
	private CascadeClassifier face_cascade_frontal;
	private CascadeClassifier face_cascade_profile;

	// / Create a constructor method
	public processor() {
		fullbody =new CascadeClassifier("./res/haarcascade_fullbody.xml");
		lowerbody =new CascadeClassifier("./res/haarcascade_lowerbody.xml");
		upperbody =new CascadeClassifier("./res/haarcascade_upperbody.xml");
		face_cascade_frontal = new CascadeClassifier(
				"./res/lbpcascade_frontalface.xml");
		face_cascade_profile = new CascadeClassifier(
				"./res/lbpcascade_profileface.xml");
		if (face_cascade_frontal.empty() || face_cascade_profile.empty()||upperbody.empty()
		 ) {
			System.out.println("--(!)Error loading A\n");
			return;
		} else {
			System.out.println("Face classifier loaded");
		}
	}

	public Mat detect(Mat inputframe) {
		Mat mRgba = new Mat();
		Mat mGrey = new Mat();
		MatOfRect faces = new MatOfRect();
		inputframe.copyTo(mRgba);
		inputframe.copyTo(mGrey);
		Imgproc.cvtColor(mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(mGrey, mGrey);

		/*
		 * fullbody.detectMultiScale(mGrey, faces);
		 * System.out.println(String.format("Detected %s faces",
		 * faces.toArray().length)); for(Rect rect:faces.toArray()) { Point
		 * center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5
		 * ); Core.ellipse( mRgba, center, new Size( rect.width*0.5,
		 * rect.height*0.5), 0, 0, 360, new Scalar( 0, 255, 0) );
		 * //Core.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x +
		 * rect.width, rect.y + rect.height), new Scalar(0, 255, 0)); }
		 */
		/*
		 * upperbody.detectMultiScale(mGrey, faces);
		 * System.out.println(String.format("Detected %s faces",
		 * faces.toArray().length)); for(Rect rect:faces.toArray()) { Point
		 * center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5
		 * ); Core.ellipse( mRgba, center, new Size( rect.width*0.5,
		 * rect.height*0.5), 0, 0, 360, new Scalar( 0, 255, 0) );
		 * //Core.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x +
		 * rect.width, rect.y + rect.height), new Scalar(0, 255, 0)); }
		 * mRgba.copyTo(mRgba);
		 */
		face_cascade_frontal.detectMultiScale(mGrey, faces);
		System.out.println(String.format("Detected %s faces",
				faces.toArray().length));
		for (Rect rect : faces.toArray()) {
			Core.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}
		mRgba.copyTo(mRgba);
		Date date = new Date();
		String filename = date+".png";
		System.out.println(String.format("Writing %s", filename));
		Highgui.imwrite(filename, mRgba);
		face_cascade_profile.detectMultiScale(mGrey, faces);
		System.out.println(String.format("Detected %s faces",
				faces.toArray().length));
		for (Rect rect : faces.toArray()) {
			Core.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}
		mRgba.copyTo(mRgba);
		Date date1 = new Date();
		String filename1 = date1+".png";
		System.out.println(String.format("Writing %s", filename1));
		Highgui.imwrite(filename1, mRgba);
		Core.flip(mGrey, mGrey, 1);

		face_cascade_profile.detectMultiScale(mGrey, faces);
		System.out.println(String.format("Detected %s faces",
				faces.toArray().length));
		for (Rect rect : faces.toArray()) {
			Core.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}
		Core.flip(mRgba, mRgba, 1);
		Date date2 = new Date();
		String filename2 = date2+".png";
		System.out.println(String.format("Writing %s", filename2));
		Highgui.imwrite(filename2, mRgba);
		return (mRgba);
	}
}

public class window {
	public static void main(String arg[]) {
		// Load the native library.
		System.loadLibrary("opencv_java2410");
		String window_name = "Capture - Face detection";
		JFrame frame = new JFrame(window_name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		processor my_processor = new processor();
		My_Panel my_panel = new My_Panel();
		frame.setContentPane(my_panel);
		frame.setVisible(true);
		// -- 2. Read the video stream
		Mat webcam_image = new Mat();
		VideoCapture capture = new VideoCapture(0);
		if (capture.isOpened()) {
			while (true) {
				capture.read(webcam_image);
				if (!webcam_image.empty()) {
					frame.setSize(webcam_image.width() + 40,
							webcam_image.height() + 60);
					// -- 3. Apply the classifier to the captured image
					webcam_image = my_processor.detect(webcam_image);
					// -- 4. Display the image
					my_panel.MatToBufferedImage(webcam_image); 
					my_panel.repaint();
				} else {
					System.out.println("No captured frame -- Break!");
					break;
				}
			}
		}
		return;
	}
}
