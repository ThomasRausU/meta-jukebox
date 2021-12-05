SUMMARY = "Extension for playing music from your local music archive."
HOMEPAGE = "https://mopidy.com/ext/local/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "29165157134fe869228da675e4d0083888368a29dc7dd3203fe1a27d7b4d83a3"

PYPI_PACKAGE = "Mopidy-Local"
inherit pypi setuptools3

RDEPENDS:${PN} += "python3-uritools"