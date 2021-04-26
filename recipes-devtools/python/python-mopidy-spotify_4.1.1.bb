SUMMARY = "Extension for playing music from the Spotify music streaming service."
HOMEPAGE = "https://mopidy.com/ext/spotify/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "e137d0675288e48563c15d50cb2722c618f1a085673f96b620e64fafdaab97af"


PYPI_PACKAGE = "Mopidy-Spotify"
inherit pypi setuptools3

RDEPENDS_${PN} += "python-pyspotify"