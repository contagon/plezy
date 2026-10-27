package com.edde746.plezy

import android.graphics.SurfaceTexture
import android.view.Surface
import com.edde746.plezy.libass.media.widget.TextureSurfaceView
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TextureSurfaceViewTest {
  @Test
  fun resizeKeepsSurfaceAndDestructionNotifiesProducerBeforeReplacement() {
    val events = mutableListOf<String>()
    val surfaces = mutableListOf<Surface>()
    val view = TextureSurfaceView(
      RuntimeEnvironment.getApplication(),
      opaque = false,
      onCreated = { surface, w, h ->
        surfaces += surface
        events += "create:$w:$h"
      },
      onResized = { w, h -> events += "resize:$w:$h" },
      onDestroyed = { events += "destroy" }
    )
    val listener = view.surfaceTextureListener!!
    assertFalse(view.isOpaque)
    val first = SurfaceTexture(0)
    listener.onSurfaceTextureAvailable(first, 1920, 1080)
    listener.onSurfaceTextureSizeChanged(first, 1280, 720)
    assertTrue(listener.onSurfaceTextureDestroyed(first))
    val second = SurfaceTexture(0)
    listener.onSurfaceTextureAvailable(second, 640, 360)
    assertEquals(listOf("create:1920:1080", "resize:1280:720", "destroy", "create:640:360"), events)
    assertEquals(2, surfaces.size)
    assertNotSame(surfaces[0], surfaces[1])
    listener.onSurfaceTextureDestroyed(second)
    first.release()
    second.release()
  }
}
