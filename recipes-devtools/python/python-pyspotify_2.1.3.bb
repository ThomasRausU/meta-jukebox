SUMMARY = "pyspotify provides a Python interface to Spotify’s online music streaming service."
HOMEPAGE = "https://pyspotify.readthedocs.io/en/latest/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"


SRC_URI = "https://github.com/mopidy/pyspotify/archive/v2.1.3.tar.gz \
		   https://github.com/mopidy/libspotify-archive/raw/master/libspotify-12.1.51-Linux-armv6-release.tar.gz;name=libspotify-12.1.51-Linux-armv6-release\
		   file://rmldconfig.patch;striplevel=2;patchdir=${S}/../libspotify-12.1.51-Linux-armv6-release"

SRC_URI[sha256sum] = "6765235cc6469c619689cdb051ad0e0c9077baaafc7cb156aae5cb14f1c43ff8"
SRC_URI[libspotify-12.1.51-Linux-armv6-release.sha256sum] = "4fb888eeb486578fa3a08e15f5aa2101632e60b56a068553d05d5d4ee0a080cc"

do_configure() {
	cd ${WORKDIR}/libspotify-12.1.51-Linux-armv6-release && prefix=${PYTHON_INCLUDE_DIR}/../../ oe_runmake install && cd -
}

do_compile() {
        cd ${DISTUTILS_SETUP_PATH}
        NO_FETCH_BUILD=1 \
        STAGING_INCDIR=${STAGING_INCDIR} \
        STAGING_LIBDIR=${STAGING_LIBDIR} \
        ${STAGING_BINDIR_NATIVE}/${PYTHON_PN}-native/${PYTHON_PN} setup.py \
        build --build-base=${B} ${DISTUTILS_BUILD_ARGS} || \
        bbfatal_log "'${PYTHON_PN} setup.py build ${DISTUTILS_BUILD_ARGS}' execution failed."
}

do_install() {
        cd ${DISTUTILS_SETUP_PATH}
        install -d ${D}${PYTHON_SITEPACKAGES_DIR}
        STAGING_INCDIR=${STAGING_INCDIR} \
        STAGING_LIBDIR=${STAGING_LIBDIR} \
        PYTHONPATH=${D}${PYTHON_SITEPACKAGES_DIR} \
        ${STAGING_BINDIR_NATIVE}/${PYTHON_PN}-native/${PYTHON_PN} setup.py \
        build --build-base=${B} install --skip-build ${DISTUTILS_INSTALL_ARGS} || \
        bbfatal_log "'${PYTHON_PN} setup.py install ${DISTUTILS_INSTALL_ARGS}' execution failed."

        # support filenames with *spaces*
        find ${D} -name "*.py" -exec grep -q ${D} {} \; \
                               -exec sed -i -e s:${D}::g {} \;

        for i in ${D}${bindir}/* ${D}${sbindir}/*; do
            if [ -f "$i" ]; then
                sed -i -e s:${PYTHON}:${USRBINPATH}/env\ ${DISTUTILS_PYTHON}:g $i
                sed -i -e s:${STAGING_BINDIR_NATIVE}:${bindir}:g $i
            fi
        done

        rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/easy-install.pth

        #
        # FIXME: Bandaid against wrong datadir computation
        #
        if [ -e ${D}${datadir}/share ]; then
            mv -f ${D}${datadir}/share/* ${D}${datadir}/
            rmdir ${D}${datadir}/share
        fi
}

DEPENDS += "python3-native python3-cffi-native python3-setuptools-native libspotify"

RDEPENDS_${PN} += "libspotify"

S = "${WORKDIR}/pyspotify-2.1.3"

DISTUTILS_SETUP_PATH = "${WORKDIR}/pyspotify-2.1.3"

inherit distutils3-base base

