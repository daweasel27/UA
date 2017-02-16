#include <iostream>
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"

using namespace cv;
using namespace std;

int a;
Mat imgOriginal;
Mat imgThresholded;
int numO = 0;

void createObj() {
	string c = "Control" + std::to_string(numO);
	namedWindow(c, CV_WINDOW_AUTOSIZE);
	a = 0; cvCreateTrackbar("LowH", c.c_str(), &a, 179);	
	a = 179; cvCreateTrackbar("HighH", c.c_str(), &a, 179);
	a = 0; cvCreateTrackbar("LowS", c.c_str(), &a, 255);
	a = 255; cvCreateTrackbar("HighS", c.c_str(), &a, 255);
	a = 0; cvCreateTrackbar("LowV", c.c_str(), &a, 255);
	a = 255; cvCreateTrackbar("HighV", c.c_str(), &a, 255);
	numO++;
}
void destroyObj() {
	numO--;
	string c = "Control" + std::to_string(numO);
	cvDestroyWindow(c.c_str());
}
void init() {
	namedWindow("Video", CV_WINDOW_AUTOSIZE); 
	createObj();
}
void detect(int number) {
	Mat imgHSV;
	string c = "Control" + std::to_string(number);
	int iLowH = cvGetTrackbarPos("LowH", c.c_str());
	int iHighH = cvGetTrackbarPos("HighH", c.c_str());
	int iLowS = cvGetTrackbarPos( "LowS", c.c_str());
	int iHighS = cvGetTrackbarPos("HighS", c.c_str());
	int iLowV = cvGetTrackbarPos("LowV", c.c_str());
	int iHighV = cvGetTrackbarPos("HighV", c.c_str());
	cvtColor(imgOriginal, imgHSV, COLOR_BGR2HSV); 
	inRange(imgHSV, Scalar(iLowH, iLowS, iLowV), Scalar(iHighH, iHighS, iHighV), imgThresholded); 
	erode(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));
	dilate(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));
	dilate(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));
	erode(imgThresholded, imgThresholded, getStructuringElement(MORPH_ELLIPSE, Size(5, 5)));
	Mat canny_output;
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	Canny(imgThresholded, canny_output, 200, 255, 3);
	findContours(canny_output, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));
	for (int i = 0; i< contours.size(); i++){
		Scalar color = Scalar((iLowH + iHighH) / 2, (iLowS + iHighS) / 2, (iLowV + iLowV) / 2);
		drawContours(imgOriginal, contours, i, color, 2, 8, hierarchy, 0, Point());
	}
	imshow(c.c_str(), imgThresholded);
}
int main(int argc, char** argv)
{
	VideoCapture cap(0); 
	if (!cap.isOpened()) {return -1;}
	init();
	while (true){
		bool bSuccess = cap.read(imgOriginal); 
		if (!bSuccess){ break;}
		for (int i = 0; i < numO; i++) {
			detect(i);
		}
		imshow("Video", imgOriginal); 
		if (waitKey(1) == 43) { createObj(); }
		if (waitKey(1) == 45) { if(numO > 0) destroyObj(); }
	}
	return 0;
}
