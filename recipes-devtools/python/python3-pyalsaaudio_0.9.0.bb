SUMMARY = "This package contains wrappers for accessing the ALSA API from Python. It is fairly complete for PCM devices and Mixer access."
HOMEPAGE = "https://pypi.org/project/pyalsaaudio/"
LICENSE = "PSF"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1a3b161aa0fcec32a0c8907a2219ad9d"

SRC_URI[sha256sum] = "3ca069c736c8ad2a3047b5033468d983a2480f94fad4feb0169c056060e01e69"

PYPI_PACKAGE = "pyalsaaudio"
inherit pypi setuptools3

DEPENDS += "alsa-lib"

RDEPENDS_${PN} += "libasound"
