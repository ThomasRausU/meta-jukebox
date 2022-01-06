SUMMARY = "Raspberry Pi Python library for SPI RFID RC522 module."
HOMEPAGE = "https://github.com/ChisSoc/pi-rc522.git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=f06bfbd6c160d3fc079137e4e0252fe8"

SRCREV = "ec881c255ac534acff4737bbc7e347ef5177ba61"
SRC_URI = "git://github.com/ChisSoc/pi-rc522.git;protocol=https;branch=master"

inherit distutils3 

S = "${WORKDIR}/git"

RDEPENDS:${PN} += "rpi-gpio \
                  python3-core \
                  python3-spidev"
                  
          
                  
DEPENDS += "rpi-gpio-native \
           python3-spidev-native"