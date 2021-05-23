FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://php.ini" 

do_install_append () {

	install -d ${D}/etc/php/apache2-php7/
	install -m 0755 ${WORKDIR}/php.ini ${D}/etc/php/apache2-php7/
}

FILES_${PN}-fpm += "/etc/php/apache2-php7/*"