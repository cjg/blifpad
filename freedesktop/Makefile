DESTDIR=/usr

all:
	sed "s|%DESTDIR%|$(DESTDIR)\/|" BlifPad.in > BlifPad
	chmod +x BlifPad

install:
	mkdir -p $(DESTDIR)/bin
	mkdir -p $(DESTDIR)/lib/BlifPad
	mkdir -p $(DESTDIR)/share/mime/packages
	mkdir -p $(DESTDIR)/share/applications
	mkdir -p $(DESTDIR)/share/pixmaps
	mkdir -p $(DESTDIR)/share/icons/hicolor/48x48/mimetypes
	cp BlifPad $(DESTDIR)/bin
	cp ../BlifPad.jar $(DESTDIR)/lib/BlifPad
	cp blifpad.xml $(DESTDIR)/share/mime/packages
	cp BlifPad.desktop $(DESTDIR)/share/applications
	cp BlifPad.png $(DESTDIR)/share/pixmaps
	cp text-blif.png $(DESTDIR)/share/icons/hicolor/48x48/mimetypes
	(cd $(DESTDIR)/share/icons/hicolor/48x48/mimetypes; ln -s text-blif.png gnome-mime-text-blif.png)
	update-mime-database $(DESTDIR)/share/mime
	gtk-update-icon-cache
