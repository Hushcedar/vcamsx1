package virtual.camera.app.util

import android.app.Application
import virtual.camera.app.app.App
import virtual.camera.app.view.apps.AppsFactory
import virtual.camera.app.view.gms.GmsFactory
import virtual.camera.app.view.list.ListFactory

object InjectionUtil {

    fun getAppsFactory(): AppsFactory =
        AppsFactory(App.getContext().applicationContext as Application)

    fun getListFactory(): ListFactory =
        ListFactory(App.getContext().applicationContext as Application)

    fun getGmsFactory(): GmsFactory =
        GmsFactory(App.getContext().applicationContext as Application)
}
