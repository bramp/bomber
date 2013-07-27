.PHONY : all

all : game/bomber-android/assets/data/bomber.txt game/bomber-android/assets/data/bomber.png

game/bomber-android/assets/data/bomber.txt : graphics/bomber.txt
	cp graphics/bomber.txt game/bomber-android/assets/data/

game/bomber-android/assets/data/bomber.png : graphics/bomber.png
	rm game/bomber-android/assets/data/bomber.png
	optipng graphics/bomber.png -out game/bomber-android/assets/data/bomber.png
