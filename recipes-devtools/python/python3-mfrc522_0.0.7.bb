DESCRIPTION = "A python library to read/write RFID tags via the budget MFRC522 RFID module."
HOMEPAGE = "https://github.com/pimylifeup/MFRC522-python"
SECTION = "devel/python"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://README.md;md5=c1ea90f4c2575f8c40da833dbb358e2b"

SRC_URI[sha256sum] = "74c7020a4fc4870f5d7022542c36143fba771055a2fae2e5929e6a1159d2bf00"

SRC_URI += "file://read.py \ 
			file://write.py"

PYPI_PACKAGE = "mfrc522"

inherit pypi setuptools3

RDEPENDS_${PN} = "rpi-gpio \
			 	  python3-spidev"
			 	  

do_install_append() {
	install -m 755 ../read.py ${D}/
	install -m 755 ../write.py ${D}/
}

		 	  
FILES_${PN} += "/read.py \
				/write.py"