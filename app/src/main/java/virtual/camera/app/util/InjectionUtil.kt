package virtual.camera.app.util

import virtual.camera.app.data.AppsRepository
import virtual.camera.app.data.GmsRepository
import virtual.camera.app.view.apps.AppsFactory
import virtual.camera.app.view.gms.GmsFactory
import virtual.camera.app.view.list.ListFactory

/**
 * InjectionUtil — dependency wiring
 *
 * Same interface as the original — the UI fragments call these
 * static factory getters. No UI changes needed.
 */
object InjectionUtil {

    fun getAppsFactory(): AppsFactory =
        AppsFactory(AppsRepository())

    fun getListFactory(): ListFactory =
        ListFactory(AppsRepository())

    fun getGmsFactory(): GmsFactory =
        GmsFactory(GmsRepository())
}
