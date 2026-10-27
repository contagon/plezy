package com.edde746.plezy.libass.media.widget

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView

/** Owns one Surface per texture lifetime; the producer must detach before destruction returns. */
class TextureSurfaceView(
  context: Context,
  opaque: Boolean = true,
  private val onCreated: (Surface, Int, Int) -> Unit,
  private val onResized: (Int, Int) -> Unit,
  private val onDestroyed: () -> Unit
) : TextureView(context),
  TextureView.SurfaceTextureListener {
  private var ownedSurface: Surface? = null

  init {
    isOpaque = opaque
    surfaceTextureListener = this
  }

  override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
    val surface = Surface(texture)
    ownedSurface = surface
    onCreated(surface, width, height)
  }

  override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
    onResized(width, height)
  }

  override fun onSurfaceTextureDestroyed(texture: SurfaceTexture): Boolean {
    try {
      onDestroyed()
    } finally {
      ownedSurface?.release()
      ownedSurface = null
    }
    return true
  }

  override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit
}
