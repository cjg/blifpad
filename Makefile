PACKAGE = BlifPad
VERSION = -$(shell date +%F-%H)

EXTRA = Makefile Manifest

all: $(PACKAGE).jar

clean:
	(cd src; make clean)
	-rm $(PACKAGE).jar

BlifPad.jar:
	(cd src; make)
	-rm -rf .jar.d
	mkdir .jar.d
	(cd .jar.d; mkdir images)
	(cd .jar.d; mkdir properties)
	cp src/*.class .jar.d
	cp properties/* .jar.d
	cp images/* .jar.d/images
	(cd .jar.d; jar cvfm ../$@ ../Manifest *.properties *.class images)
	rm -rf .jar.d

dist-prepare:
	mkdir -p dists
	rm -rf dists/$(PACKAGE)$(VERSION)
	mkdir -p dists/$(PACKAGE)$(VERSION)
#mkdir -p $(foreach i, $(shell find $(PACKAGE) -type d), dists/$(PACKAGE)$(VERSION)/$(i))
	mkdir -p dists/$(PACKAGE)$(VERSION)/images $(foreach i, $(shell find images -type d), dists/$(PACKAGE)$(VERSION)/$(i))

%.java-dist: %.java
	cp $< dists/$(PACKAGE)$(VERSION)/$(shell dirname $<)

%-image-dist:
	cp images/$(@:-image-dist=) dists/$(PACKAGE)$(VERSION)/images/$(@:-image-dist=) 

dist: dist-prepare $(SOURCES:.java=.java-dist) $(IMAGES:=-image-dist)
	cp $(EXTRA) dists/$(PACKAGE)$(VERSION)
	(cd dists; tar cfz $(PACKAGE)$(VERSION).tar.gz $(PACKAGE)$(VERSION))
	rm -rf dists/$(PACKAGE)$(VERSION)
