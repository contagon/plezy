import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:plezy/mpv/player/platform/player_android.dart';
import 'package:plezy/services/settings_service.dart';

import '../test_helpers/mock_player_channels.dart';
import '../test_helpers/prefs.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  for (final enabled in [false, true]) {
    test('screen capture compatibility $enabled reaches ExoPlayer startup', () async {
      resetSharedPreferencesForTest(initialAsync: {'screen_capture_compatibility': enabled});
      await SettingsService.getInstance();
      MethodCall? initialize;
      await withMockPlayerChannels(
        methodChannelName: 'com.plezy/exo_player',
        eventChannelName: 'com.plezy/exo_player/events',
        methodHandler: (call) async {
          if (call.method == 'initialize') initialize = call;
          return call.method == 'initialize' ? true : null;
        },
        testBody: () async {
          final player = PlayerAndroid();
          try {
            await player.requestAudioFocus();
          } finally {
            await player.dispose();
          }
        },
      );
      expect((initialize!.arguments as Map)['screenCaptureCompatibility'], enabled);
    });
  }
}
