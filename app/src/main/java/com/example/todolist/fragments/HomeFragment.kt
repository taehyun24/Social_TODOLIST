package com.example.todolist.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.MemoDetailActivity
import com.example.todolist.R
import com.example.todolist.CustomToast
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.model.Memo
import com.example.todolist.viewmodel.MemoViewModel
import com.example.todolist.viewmodel.MemoViewModelFactory
import com.example.todolist.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment() : Fragment() {

    lateinit var memoViewModelFactory: MemoViewModelFactory
    private var binding: FragmentHomeBinding? = null
    lateinit var memoViewModel: MemoViewModel
    lateinit var profileViewModel: ProfileViewModel
    lateinit var today: Calendar
    lateinit var date: String
    lateinit var now_date: String
    var auth: FirebaseAuth? = null //유저 정보가져오기 위해 사용
    var uid: String? = null
    var email: String? = null
    var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        if (arguments!= null){  //소셜 탭에서 넘어온경우
            email = arguments?.getString("email")
            uid = arguments?.getString("uid")
            binding?.tvHome?.text = email
            binding?.todoBtn?.visibility = View.INVISIBLE
            binding?.followBtn?.visibility = View.VISIBLE
        }else{  //사용자가 로그인한 경우
            email = auth?.currentUser?.email
            uid = auth?.currentUser?.uid
            binding?.todoBtn?.visibility = View.VISIBLE
            binding?.followBtn?.visibility = View.INVISIBLE
        }






        //팔로우버튼 눌렀을때
        binding?.followBtn?.setOnClickListener {
            profileViewModel.requestFollow(uid!!)
            if (binding?.followBtn?.text == "팔로우"){
                CustomToast.createToast(requireContext(),"팔로우했어요")?.show()
            }else{
                CustomToast.createToast(requireContext(),"팔로우를 취소했어요")?.show()
            }

        }

        //현재 날짜 구하기
        var now = System.currentTimeMillis()
        var get_date = Date(now)
        var dataFormat = SimpleDateFormat("yyyy년M월dd일")
        date = dataFormat.format(get_date)
        now_date = dataFormat.format(get_date)
        binding?.tvDate?.text = date    //현재 날짜
        getDateDay(date,"yyyy년M월dd일")    //요일구하기

        //뷰모델 불러오기
        memoViewModelFactory = MemoViewModelFactory(email!!, uid!!, date)
        memoViewModel = ViewModelProvider(this,memoViewModelFactory).get(MemoViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        //뷰모델 관찰
        memoViewModel.currentValue.observe(requireActivity(), Observer {
            binding?.memoRecyclerView?.adapter = CustomAdapter()
            binding?.memoRecyclerView?.layoutManager = LinearLayoutManager(activity)
        })

        profileViewModel.profilecurrentValue.observe(requireActivity(), Observer {
            for (i:Int in 0 until profileViewModel.profileList.size){
                //내 계정의 팔로잉에 상대방의 uid를 포함하고있을때
                if(profileViewModel.profileList[i].uid?.equals(auth?.currentUser?.uid)!!){
                    var myUID = profileViewModel.profileList[i]
                    if (myUID.followings.keys.contains(uid)){
                        binding?.followBtn?.text = "팔로우 취소"
                    }else{
                        binding?.followBtn?.text = "팔로우"
                    }
                    break
                }
            }


        })

        //달력 눌렀을때
        binding?.calendarBtn?.setOnClickListener {
            today = Calendar.getInstance()
            val year = today.get(Calendar.YEAR)
            val month = today.get(Calendar.MONTH)
            val day = today.get(Calendar.DATE)

            val dlg = DatePickerDialog(requireContext(),object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int){
                    date = "${year}년${month+1}월${dayOfMonth}일"
                    Log.d("데이트",date)
                    binding?.tvDate?.text = date
                    getDateDay(date,"yyyy년M월dd일")    //요일구하기
                    memoViewModel.getData(date,email!!)
                }

            },year,month,day)

            dlg.show()

        }

        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //할일 버튼 눌렀을때
        binding?.todoBtn?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date",date)
            bundle.putString("uid",uid)
            bundle.putString("email",email)
            val todoFragment = TodoFragment()
            todoFragment.arguments = bundle
            todoFragment.show(requireFragmentManager(),"Dialog Fragment")
        }
    }

    inner class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ItemViewHolder>() {

        var memoList: ArrayList<Memo> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
           var view =
                LayoutInflater.from(parent.context).inflate(R.layout.memo_item, parent, false)
            return ItemViewHolder(view)
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val re_text = itemView.findViewById<TextView>(R.id.re_text)
            val memoBtn = itemView.findViewById<ImageButton>(R.id.memo_detail_btn)
            val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)

            fun bind(memo: Memo) {
                re_text.text = memo.name
            }

        }

        override fun getItemCount(): Int {
            memoList.clear()    // 이 함수가 계속 반복되서 넣었음.(이유 나중에 알아보기)
            for (i: Int in 0 until memoViewModel.memoList.size){
                if (memoViewModel.memoList[i].date == date){
                    memoList.add(memoViewModel.memoList[i])
                }
            }
            return memoList.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(memoList[position])

            holder.checkBox.isChecked = memoList[position].check_status == true


            if (binding?.followBtn?.visibility == View.VISIBLE)      //다른 사람 계정인경우
            {
               // holder.memoBtn.setImageResource(R.drawable.home_edit)
                holder.memoBtn.setOnClickListener{
                    //응원 버튼 기능
                    memoViewModel.updateCheerUp(position)
                }

                if (memoList[position].cheerup.containsKey(auth?.currentUser?.uid)){
                    //응원 버튼이 눌린경우

                    holder.memoBtn.setImageResource(R.drawable.cheer_up_edit)
                }
                else{
                    //응원 버튼이 안눌린경우
                    holder.memoBtn.setImageResource(R.drawable.cheer_up_empty_edit)
                }
            }

            else        //내 계정인경우
            {
                holder.memoBtn.setImageResource(R.drawable.ellipsis_edit)
                holder.memoBtn.setOnClickListener {
                    var intent = Intent(requireContext(),MemoDetailActivity::class.java)
                    intent.putExtra("name",memoList[position].name)
                    var arrayList: ArrayList<String> = arrayListOf()
                    for (i: Int in 0 until memoList[position].cheerup.keys.size){
                        arrayList.add(memoList[position].cheerup.keys.elementAt(i))
                    }
                    intent.putExtra("userID",arrayList)
                    startActivity(intent)
                }

                if (memoList[position].date == binding?.tvDate?.text){

                    //체크박스가 선택된 상태일 경우
                    if (holder.checkBox.isChecked){

                        holder.checkBox.setOnClickListener {
                            memoViewModel.updateCheckBox(position,false)
                            holder.checkBox.isChecked = false
                            profileViewModel.updateGauge("minus")
                            CustomToast.createToast(context!!,"취소했어요..")?.show()
                        }
                    }
                    else{

                        holder.checkBox.setOnClickListener {
                            memoViewModel.updateCheckBox(position,true)
                            holder.checkBox.isChecked = true
                            profileViewModel.updateGauge("plus")
                            CustomToast.createToast(context!!,"완료했어요!!")?.show()
                        }
                    }
                }
            }
        }
    }
    //요일 구하기
    @Throws(Exception::class)
    fun getDateDay(date: String?, dateType: String?) {
        var day = ""
        val dateFormat = SimpleDateFormat(dateType)
        val nDate = dateFormat.parse(date)
        val cal = Calendar.getInstance()
        cal.time = nDate
        val dayNum = cal[Calendar.DAY_OF_WEEK]
        when (dayNum) {
            1 -> day = "일요일"
            2 -> day = "월요일"
            3 -> day = "화요일"
            4 -> day = "수요일"
            5 -> day = "목요일"
            6 -> day = "금요일"
            7 -> day = "토요일"
        }
        binding?.tvDay?.text = day
    }

    //뷰 사라질때 binding 널값
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}