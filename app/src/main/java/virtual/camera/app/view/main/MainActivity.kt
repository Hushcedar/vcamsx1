package virtual.camera.app.view.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.edit
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.hack.opensdk.HackApi
import virtual.camera.app.R
import virtual.camera.app.app.App
import virtual.camera.app.app.AppManager
import virtual.camera.app.databinding.ActivityMainBinding
import virtual.camera.app.util.Resolution
import virtual.camera.app.util.inflate
import virtual.camera.app.view.apps.AppsFragment
import virtual.camera.app.view.base.LoadingActivity
import virtual.camera.app.view.list.ListActivity
import virtual.camera.app.view.setting.SettingActivity


class MainActivity : LoadingActivity() {

    private val viewBinding: ActivityMainBinding by inflate()

    private lateinit var mViewPagerAdapter: ViewPagerAdapter

    private val fragmentList = mutableListOf<AppsFragment>()

    private var currentUser = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbarLayout.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        initViewPager()
        initFab()
        initToolbarSubTitle()
        // Fix 1: replaced deprecated showDialog() with DialogUtil.showTipsDialog()
        DialogUtil.showTipsDialog(this)
    }

    private fun initToolbarSubTitle() {
        updateUserRemark(0)
        try {
            viewBinding.toolbarLayout.toolbar.getChildAt(1)?.setOnClickListener {
                try {
                    MaterialDialog(this).show {
                        title(res = R.string.userRemark)
                        input(
                            hintRes = R.string.userRemark,
                            prefill = viewBinding.toolbarLayout.toolbar.subtitle
                        ) { _, input ->
                            try {
                                AppManager.mRemarkSharedPreferences.edit {
                                    putString("Remark$currentUser", input.toString())
                                    viewBinding.toolbarLayout.toolbar.subtitle = input
                                }
                            } catch (e: Exception) { e.printStackTrace() }
                        }
                        positiveButton(res = R.string.done)
                        negativeButton(res = R.string.cancel)
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun initViewPager() {
        val userList = HackApi.getAvailableUserSpace()
        userList.forEach {
            fragmentList.add(AppsFragment.newInstance(it))
        }

        currentUser = userList.firstOrNull() ?: 0
        fragmentList.add(AppsFragment.newInstance(userList.size))

        mViewPagerAdapter = ViewPagerAdapter(this)
        mViewPagerAdapter.replaceData(fragmentList)
        viewBinding.viewPager.adapter = mViewPagerAdapter
        viewBinding.dotsIndicator.setViewPager2(viewBinding.viewPager)
        viewBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentUser = fragmentList[position].userID
                updateUserRemark(currentUser)
                showFloatButton(true)
            }
        })
    }

    private fun initFab() {
        viewBinding.fab.setOnClickListener {
            val userId = viewBinding.viewPager.currentItem
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("userID", userId)
            apkPathResult.launch(intent)
        }
    }

    fun showFloatButton(show: Boolean) {
        val tranY: Float = Resolution.convertDpToPixel(120F, App.getContext())
        val time = 200L
        if (show) {
            viewBinding.fab.animate().translationY(0f).alpha(1f).setDuration(time).start()
        } else {
            viewBinding.fab.animate().translationY(tranY).alpha(0f).setDuration(time).start()
        }
    }

    fun scanUser() {
        val userList = HackApi.getAvailableUserSpace()
        if (fragmentList.size == userList.size) {
            fragmentList.add(AppsFragment.newInstance(fragmentList.size))
        } else if (fragmentList.size > userList.size + 1) {
            fragmentList.removeLast()
        }
        mViewPagerAdapter.notifyDataSetChanged()
    }

    private fun updateUserRemark(userId: Int) {
        var remark = AppManager.mRemarkSharedPreferences.getString("Remark$userId", "User $userId")
        if (remark.isNullOrEmpty()) remark = "User $userId"
        viewBinding.toolbarLayout.toolbar.subtitle = remark
    }

    private val apkPathResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { data ->
                    val userId = data.getIntExtra("userID", 0)
                    val source = data.getStringExtra("source")
                    if (source != null) {
                        fragmentList[userId].installApk(source)
                    }
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_setting -> {
                SettingActivity.start(this)
            }
            R.id.killApps -> {
                Toast.makeText(this, "Apps killed", Toast.LENGTH_SHORT).show()
            }
            R.id.open_source -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/andvipgroup/VCamera"))
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "Could not open browser", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
