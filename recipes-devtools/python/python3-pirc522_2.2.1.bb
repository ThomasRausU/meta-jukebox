SUMMARY = "Raspberry Pi Python library for SPI RFID RC522 module."
HOMEPAGE = "https://github.com/ondryaso/pi-rc522"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://PKG-INFO;md5=be86bc663922d43c3d9e05719d38b890"

SRC_URI = "https://files.pythonhosted.org/packages/84/f2/e3d02257949e9caa9bff26044e0185e743ff45d3e0e099a93880e10bf718/pi-rc522-2.2.1.tar.gz"

SRC_URI[sha256sum] = "438a65a8a50a824e800353e3642878f44e410444b67b028fc25474fb19e653f8"

inherit distutils3 

S = "${WORKDIR}/pi-rc522-2.2.1"

RDEPENDS:${PN} += "rpi-gpio \
                  python3-core \
                  python3-spidev"
                  
          
                  
DEPENDS += "rpi-gpio-native \
           python3-spidev-native"