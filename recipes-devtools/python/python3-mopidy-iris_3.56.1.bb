SUMMARY = "Fully-featured Mopidy frontend client"
HOMEPAGE = "https://github.com/jaedb/iris"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=49b9427a46e471a13cb4451cf9a3503a"
SRC_URI += "file://mopidy-iris" 

SRC_URI[sha256sum] = "4cffd8ffcdad830f184ad37ef86df26bb476a2556f5f8a22b189cb35ab38b6ce"

PYPI_PACKAGE = "Mopidy-Iris"
inherit pypi setuptools3

RDEPENDS:${PN} += "bash"

do_install:append () {
install -d ${D}/etc/sudoers.d/
echo "mopidy ALL=NOPASSWD: ${PYTHON_SITEPACKAGES_DIR}/mopidy_iris/system.sh" > ${D}/etc/sudoers.d/mopidy-iris
install -d ${D}/etc/nginx/sites-enabled/
install -d ${D}/etc/nginx/sites-available/
install -m 0644 ../mopidy-iris ${D}/etc/nginx/sites-available/
ln -sf 	../sites-available/mopdiy-iris ${D}/etc/nginx/sites-enabled/
}

FILES:${PN} += "/etc/sudoers.d/mopidy-iris \
				/etc/nginx/sites-enabled/mopidy-iris \
				/etc/nginx/sites-available/mopidy-iris \
				"