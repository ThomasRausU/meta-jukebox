SUMMARY = "Frontend that provides a full MPD server implementation to make Mopidy available from MPD clients."
HOMEPAGE = "https://mopidy.com/ext/mpd/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI[sha256sum] = "d34011dad9a053c149a408c25b0ff552406513063bc9cdaab2bde30e71f81228"

PYPI_PACKAGE = "Mopidy-MPD"
inherit pypi setuptools3

RDEPENDS:${PN} = "python3-charset-normalizer"