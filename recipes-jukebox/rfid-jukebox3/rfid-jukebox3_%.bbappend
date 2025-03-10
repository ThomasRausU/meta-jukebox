RFID_JUKEBOX_MP3_FOLDER ?= ""

do_install:append () {


# this overwrite default settings with sample settings
cat <<EOF >${D}${SETTINGS_PATH}/cards.yaml
'1':
  package: volume
  plugin: ctrl
  method: set_volume
  kwargs:
    volume: 12
'2':
  package: volume
  plugin: ctrl
  method: set_volume
  args: [12]
  ignore_same_id_delay: true
'3':
  package: volume
  plugin: ctrl
  method: set_volume
  args: 12
'4':
  package: volume
  plugin: ctrl
  method: set_volume
  args: [12b]
'5':
  package: volume
  plugin: ctrl
  method: mute
'6':
  package: volume
  plugin: ctrl
  method: mute
  kwargs: {}
  ignore_card_removal_action: true
'7':
  package: volume
  plugin: ctrl
  method: get_volume
'8':
  package: volume
  plugin: ctrl
  method: set_volume
  args: [18]
'7896':
  package: volume
  plugin: ctrl
  method: set_volume
  args:
  - 12
  - erasdf
  kwargs:
    aparam: 23
    bparam: sdfs
    cparam: 56
T:
  package: player
  plugin: ctrl
  method: play_card
  args: [path/to/music/folder]
G:
  alias: play_card
  args: path/to/music/folder
V+:
  alias: change_volume
  args: 3
  ignore_card_removal_action: true
V-:
  alias: change_volume
  args: -3
  ignore_card_removal_action: true
'1347557936':
  alias: play_single
  args:
  - 03_Jungle Boogie - Jungle Boogie.mp3
'1393947156':
  alias: play_single
  args:
  - Janis Joplin - Son Of A Preacher Man.mp3
EOF

# this overwrite default jukebox settings with sample settings
cat <<EOF >${D}${SETTINGS_PATH}/jukebox.yaml
# IMPORTANT:
# Do not use paths with '~/some/dir' - always use '/home/pi/some/dir'
# Sole (!) exception is in playermpd.mpd_conf
system:
  box_name: Jukebox
modules:
  named:
    # Do not change the order!
    publishing: publishing
    volume: volume
    bluetooth_audio_buttons: controls.bluetooth_audio_buttons
    jingle: jingle
    jingle.alsawave: jingle.alsawave
    jingle.jinglemp3: jingle.jinglemp3
    player: playermpd
    
    cards: rfid.cards
    rfid: rfid.reader
    timers: timers
    host: hostif.linux
    gpio: gpio.gpio_rpi
    # rfidpinaction: rfid.pinaction.rpi
  others:
  - music_cover_art
  - misc
pulse:
  # Reset system volume to this level after start. (Comment out disables and volume is not changed)
#  startup_volume: 40
  # Automatically toggle output between primary and secondary audio output
  # when a device connects (Note: So far, toggle happens when ANY device connects)
  toggle_on_connect: true
  # Limit maximum volume range to XX % - can be changed through the UI (for temporary use)
#  soft_max_volume: 70
  # Run the tool run_configure_audio.py to configure the pulseaudio sinks
  #
  # After startup, the audio output defaults to primary
  # Any Bluetooth device should be the secondary (as it may not always be available directly after boot)
  #
  # For manual setting of pulseaudio sinks use:
  # $ pactl list sinks short
  #
  # Test sinks with
  # $ paplay -D sink_name /usr/share/sounds/alsa/Front_Center.wav
  #
  # Parameters:
  # - alias       : user defined alias for better readability
  # - volume_limit: a hard volume limit representing the new 100 %
  #                 i.e. the user input range 0...100 % will be scaled into this range
  #                 this can be used to attenuate the maximum volume levels between different outputs
  #                 i.e. make both channels equally load out 100 % and make sure it never gets too loud
  outputs:
    primary:
      alias: Built-in speakers
      volume_limit: 100
      soft_max_volume: 100
      # Leave pulse_sink_name empty for first start-up (will be filled with systems default sink)
      # pulse_sink_name: alsa_output.pci-0000_00_1f.3.analog-stereo
    secondary:
      alias: Bluetooth headset
      volume_limit: 100
      soft_max_volume: 100
      # Leave pulse_sink_name unfilled to disable secondary output
      # pulse_sink_name: bluez_sink.C4_FB_20_63_CO_FE.a2dp_sink
jingle:
   # Supported file formats as jingles: *.wav and *.mp3. Note that *.wav is faster!
  startup_sound: ../../resources/audio/startupsound.wav
  # startup_sound: ../../resources/audio/startupsound.mp3
  # Shutdown sound is not played on Ctrl-C (to reduce developer's stain)
  shutdown_sound: ../../resources/audio/shutdownsound.wav
  # Use file ending to choose player from available jingle services
  service: auto
  # The playback volume of the jingles. (Comment out means use current system volume)
#  volume: 30
jinglemp3:
  # Config of the MP3 Jingle Service
  # These parameters are passed to the mpeg123 player (e.g. [-d 2])
  call_parameters: []
alsawave:
  # Config of the Wave through ALSA Jingle Service
  device: default
playermpd:
  host: localhost
  status_file: ../../shared/settings/music_player_status.json
  second_swipe_action:
    # Note: Does not follow the RPC alias convention (yet)
    # Must be one of: 'toggle', 'play', 'skip', 'rewind', 'replay', 'none'
    alias: toggle
  library:
    update_on_startup: true
    check_user_rights: true
  mpd_conf: ~/.config/mpd/mpd.conf
rpc:
  tcp_port: 5555
  websocket_port: 5556
publishing:
  # Ports for the publisher. Setting a port number to 0 disables the port
  # The WebUI requires the websocket connection
  tcp_port: 5558
  websocket_port: 5557
rfid:
  reader_config: ../../shared/settings/rfid.yaml
  card_database: ../../shared/settings/cards.yaml
gpio:
  gpio_rpi_config: ../../shared/settings/gpio.yaml
timers:
  # These timers are always disabled after start
  # The following values only give the default values.
  # These can be changed when enabling the respective timer on a case-by-case basis w/o saving
  shutdown:
    default_timeout_sec: 3600
  stop_player:
    default_timeout_sec: 3600
  volume_fade_out:
    default_time_per_iteration_sec: 900
    number_of_steps: 10
host:
  # Do not actually execute system shutdown and reboot commands
  debug_mode: false
  # Whether to publish the host's temperature regularly
  publish_temperature:
    enabled: true
    timer_interval_sec: 10
  # Should we disable the power management of the WLAN to ensure reachability via WLAN?
  wlan_power:
    disable_power_down: False
    # Specify the wlan interface. Get a list from $iwconfig. 'wlan0' is default on RPis
    card: wlan0
  # RPi-specific stuff. Will be silently ignored if not on an RPi
  rpi:
    hdmi_power_down: False
bluetooth_audio_buttons:
  # When a bluetooth sound device (headphone, speakers) connects
  # attempt to automatically listen to it's buttons (play, next, ...)
  # Note that volume up/down is handled independently from this by the bluetooth audio protocol
  enable: true
# Espeak is the current speech synthesizer
speaking_text:
  # All languages available here: http://espeak.sourceforge.net/languages.html
  lang: en
  # Must be one of: normal, fast, slow
  speed: normal
  # Whether you want to read punctuation or not
  speak_punct: False
  # Must be one of: female, male, croak, whisper
  voice: female


EOF


#install mp3s
install -d -o pi ${D}${INSTALLATION_PATH}/shared/audiofolders

if [ "${RFID_JUKEBOX_MP3_FOLDER}" != "" ] ; then
		cp -r ${RFID_JUKEBOX_MP3_FOLDER} ${D}${INSTALLATION_PATH}/shared/audiofolders
		chown -R pi: ${D}${INSTALLATION_PATH}/shared/audiofolders/*
fi
}

# use local source instead of github
# inherit externalsrc
# EXTERNALSRC = "/home/tro/git/RPi-Jukebox-RFID"
# EXTERNALSRC_BUILD = "/home/tro/git/RPi-Jukebox-RFID"

    