FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

do_install_append () {

	install -d /etc/php/apache2-php7/
	install -m 0755 ${WORKDIR}/php.ini ${D}/etc/php/apache2-php7/
}