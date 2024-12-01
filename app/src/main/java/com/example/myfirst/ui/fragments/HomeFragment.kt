package com.example.myfirst.ui.fragments

//import com.example.myfirst.data.entity.UserDB
//import com.example.myfirst.ui.activities.SignInActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myfirst.data.entity.MyProf
import com.example.myfirst.data.entity.Student
import com.example.myfirst.databinding.FragmentHomeBinding
import com.example.myfirst.mvvm.HomeFragMVVM
import com.example.myfirst.ui.activities.SignInActivity
import com.example.myfirst.utils.HomeCustomFactory

//import pl.droidsonroids.gif.GifImageView

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var homeFragMVVM: HomeFragMVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingCase()
        homeFragMVVM = ViewModelProvider(this,
            HomeCustomFactory(sPref.getString("saved_token", "").toString(), context))[HomeFragMVVM::class.java]

        homeFragMVVM.observeStudent().observe(viewLifecycleOwner, object : Observer<Student> {
            override fun onChanged(value: Student) {
                println("value1 $value")
                if(value != null){
                    //saveUser(value)
                    val mealImage = binding.ivAvatar
                    val imageUrl = value.Photo?.UrlSmall.toString()
                    val fIO = value.FIO
                    Glide.with(this@HomeFragment)
                        .load(imageUrl)
                        .circleCrop()
                        .into(mealImage)
                    binding.tvNameStudent.text = fIO.toString()
                    cancelLoadingCase(context)
                }else{
                    cancelLoadingCase(context)
                    Toast.makeText(context, "Не актуальный токен или проблема с сервером. Уже не то)", Toast.LENGTH_SHORT).show()
                }
            }
        })
        homeFragMVVM.observeRandomMeal().observe(viewLifecycleOwner, object : Observer<MyProf> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onChanged(value: MyProf) {
                println("value2 $value")
                if(value != null){
                    val birthDate = value.BirthDate
                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val formatter = SimpleDateFormat("dd.MM.yyyy")
                    val output: String = formatter.format(parser.parse(birthDate))
                    binding.BirthDateUser.text = output
                    val iD = value.StudentCod
                    binding.studentCodUser.text = iD
                    val course = value.RecordBooks[0].Course
                    binding.CourseUser.text = course
                    val faculty = value.RecordBooks[0].Faculty
                    binding.FacultyUser.text = faculty
                    val specialty = value.RecordBooks[0].Specialty
                    binding.DirectionUser.text = specialty
                    val emailUs = value.Email
                    binding.EmailUser.text = emailUs
                    cancelLoadingCase(context)
                }else{
                    cancelLoadingCase(context)
                    Toast.makeText(context, "Не актуальный токен или проблема с сервером. Уже не то)", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.bLogout.setOnClickListener{
            //deleteUser()
            openRunTimeSignInActivity()
        }
    }

    private fun showLoadingCase() {
        /*binding.apply {
            ivPhotoMgu.visibility = View.INVISIBLE
            background.visibility = View.INVISIBLE
            items.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_loading))
        }*/
    }

    companion object{
        fun cancelLoadingCase(context:Context?) {
            /*val activity = context as? Activity
            val ivPhotoMgu = activity?.findViewById(R.id.iv_photo_mgu) as? ImageView
            val background = activity?.findViewById(R.id.background) as? FrameLayout
            val items = activity?.findViewById(R.id.items) as? LinearLayout
            val loadingGif = activity?.findViewById(R.id.loading_gif) as? GifImageView
            val rootHome = activity?.findViewById(R.id.root_home) as? ConstraintLayout
            ivPhotoMgu?.visibility = View.VISIBLE
            background?.visibility = View.VISIBLE
            items?.visibility = View.VISIBLE
            loadingGif?.visibility = View.INVISIBLE
            rootHome?.setBackgroundColor(ContextCompat.getColor(activity, R.color.white))*/
        }
    }

    private fun openRunTimeSignInActivity(){
        val intent: Intent = Intent(requireActivity(), SignInActivity::class.java)
        sPref.edit().putString("saved_token", "").commit()
        sPref.edit().putString("saved_refresh_token", "").commit()
        sPref.edit().putString("date_sign_in", "").commit()
        startActivity(intent)
    }

    /*private fun deleteUser() {
        homeFragMVVM.deleteUserById("1")
    }

    private fun saveUser(student: Student) {
        val user = UserDB(student.Id.toString(),
            student.UserName.toString(),
            student.FIO.toString(),
            student.Photo?.UrlSmall.toString())
        homeFragMVVM.insertUser(user)
    }*/
}