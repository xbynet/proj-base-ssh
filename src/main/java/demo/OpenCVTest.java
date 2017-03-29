package demo;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_highgui.*;	//imshow()位于此
import static org.bytedeco.javacpp.opencv_imgproc.*;	//COLOR_RGB2GRAY
/*import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;*/

//http://blog.csdn.net/poem_qianmo/article/details/19925819 opencv的包结构
public class OpenCVTest {
	/**
	 * 人脸识别 http://docs.opencv.org/2.4.4-beta/doc/tutorials/introduction/desktop_java/java_dev_intro.html
	 */
/*	public void testFaceDetect() {
		System.out.println("\nRunning DetectFaceDemo");

		// Create a face detector from the cascade file in the resources
		// directory.
		CascadeClassifier faceDetector = new CascadeClassifier(
				"D:\\code\\lbpcascade_frontalface.xml");
		Mat image = Highgui.imread("D:\\code\\lena.png");

		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		System.out.println(String.format("Detected %s faces",
				faceDetections.toArray().length));

		// Draw a bounding box around each face.
		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}

		// Save the visualized detection.
		String filename = "D:\\code\\faceDetection.png";
		System.out.println(String.format("Writing %s", filename));
		Highgui.imwrite(filename, image);
	}
*/
	/**
	 * 灰度化、二值化 http://blog.csdn.net/liyuqian199695/article/details/53925046
	 */
/*	public void testThresh() {
		try {
			
			// 读取原图像
			Mat image = Highgui.imread("D:\\code\\lena.png");
			// 建立灰度图像存储空间
			Mat dst = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
			// 彩色图像灰度化
			Imgproc.cvtColor(image, dst, Imgproc.COLOR_RGB2GRAY);
			// 保存灰度图像
			Highgui.imwrite("D:\\code\\gray.jpg", dst);
			// 建立图像二值化存储空间
			Mat B_img = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
			// 图像二值化  函数说明：第一个参数表示输入图像，必须为单通道灰度图。第二个参数表示输出的边缘图像，为单通道黑白图。第三个参数表示阈值。第四个参数表示最大值。第五个参数表示运算方法。
			Imgproc.threshold(dst, B_img, 100, 255, Imgproc.THRESH_TOZERO);
			// 图像保存输出
			Highgui.imwrite("D:\\code\\thresh.png", B_img);
		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
		}
	}*/

	/**
	 * javacv方式灰度化、二值化 http://blog.csdn.net/liyuqian199695/article/details/53931756
	 */
	public void testjavacv(){
		Mat image=imread("D:\\code\\lena.png");	//加载图像
		if(image.empty())	
		{
			System.out.println("图像加载错误，请检查图片路径！");
			return ;
		}
		imshow("原始图像",image); 	
		Mat gray=new Mat();
		cvtColor(image,gray,COLOR_RGB2GRAY);		//彩色图像转为灰度图像
		imshow("灰度图像",gray);
		Mat bin=new Mat();
		threshold(gray,bin,120,255,THRESH_TOZERO); 	//图像二值化
		imshow("二值图像",bin);
		// 等待3000 ms后窗口自动关闭  ，0为不关闭
		waitKey(3000);
	}
	public static void main(String[] args) {
		System.out.println("Hello, OpenCV");

		// Load the native library. 加载opencv库
//		System.loadLibrary("opencv_java2413");
		new OpenCVTest().testjavacv();
	}
}
