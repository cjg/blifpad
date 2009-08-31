all: BlifPad.app

BlifPad.app:
	-rm -rf BlifPad.app
	mkdir BlifPad.app
	mkdir BlifPad.app/Contents
	mkdir BlifPad.app/Contents/MacOS
	mkdir BlifPad.app/Contents/Resources
	mkdir BlifPad.app/Contents/Resources/Java
	cp /System/Library/Frameworks/JavaVM.framework/Versions/Current/Resources/MacOS/JavaApplicationStub BlifPad.app/Contents/MacOS
	cp Info.plist BlifPad.app/Contents
	cp PkgInfo BlifPad.app/Contents
	cp ../BlifPad.jar BlifPad.app/Contents/Resources/Java
	cp BlifPad.icns BlifPad.app/Contents/Resources
	cp blif.icns BlifPad.app/Contents/Resources
	/Developer/Tools/SetFile -a B BlifPad.app

clean:
	rm -rf BlifPad.app
