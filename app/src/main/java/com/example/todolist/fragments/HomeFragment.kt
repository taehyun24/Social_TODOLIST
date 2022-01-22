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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.MemoDetailActivity
import com.example.todolist.R
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.model.Memo
import com.example.todolist.viewmodel.MemoViewModel
import com.example.todolist.viewmodel.MemoViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    lateinit var memoViewModelFactory: MemoViewModelFactory
    private var binding: FragmentHomeBinding? = null
    lateinit var memoViewModel: MemoViewModel
    lateinit var today: Calendar
    lateinit var date: String
    var auth: FirebaseAuth? = null //유저 정보가져오기 위해 사용
    var uid: String? = null
    var currentUserUid: String? = null // 내계정인지 다른사람계정인지 파악하기위해 사용

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        if (arguments!= null){  //소셜 탭에서 넘어온경우
            uid = arguments?.getString("uid")
        }else{  //사용자가 로그인한 경우
            uid = auth?.uid
        }
        currentUserUid = auth?.currentUser?.uid

        if (uid == currentUserUid){
            //내 계정인 경우 EP11 유튜브

        }else{
            //다른 사람 계정인 경우
        }

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

                    binding?.memoRecyclerView?.adapter = CustomAdapter()
                    binding?.memoRecyclerView?.layoutManager = LinearLayoutManager(activity)
                }
            },year,month,day)
            dlg.show()
        }

        var now = System.currentTimeMillis()
        var get_date = Date(now)
        var dataFormat = SimpleDateFormat("yyyy년M월dd일")
        date = dataFormat.format(get_date)
        binding?.tvDate?.text = date    //현재 날짜
        getDateDay(date,"yyyy년M월dd일")    //요일구하기

        memoViewModelFactory = MemoViewModelFactory("email!!", uid!!)
        memoViewModel = ViewModelProvider(this,memoViewModelFactory).get(MemoViewModel::class.java)



        memoViewModel.currentValue.observe(requireActivity(), Observer {
            binding?.memoRecyclerView?.adapter = CustomAdapter()
            binding?.memoRecyclerView?.layoutManager = LinearLayoutManager(activity)
        })

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.todoBtn?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("date",date)
            bundle.putString("uid",uid)
            val todoFragment = TodoFragment()
            todoFragment.arguments = bundle
            todoFragment.show(requireFragmentManager(),"Dialog Fragment")
        }
    }

    inner class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ItemViewHolder>() {

        var memoList: ArrayList<Memo> = arrayListOf()


        /*
        init {
            var s = memoViewModel.db?.collection("memo")?.
            notifyDataSetChanged()

            /*memoViewModel.db?.collection("memo")?.addSnapshotListener { value, error ->
                for (snapshot in value!!.documents) {
                    memoList.add(snapshot.toObject(Memo::class.java)!!)     //파이어베이스에서 가져온값을 memoList에 넣음
                }
                notifyDataSetChanged()
            }*/
        }*/

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
           var view =
                LayoutInflater.from(parent.context).inflate(R.layout.memo_item, parent, false)

            return ItemViewHolder(view)
        }

        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val re_text = itemView.findViewById<TextView>(R.id.re_text)
            val memoBtn = itemView.findViewById<Button>(R.id.memo_detail_btn)

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
            /*
            memoViewModel.currentValue.observe(requireActivity(), Observer {
                if(memoViewModel.memoList[position].date == date){
                    holder.bind(it[position])
                }
                re_text.text = it[position].name
                holder.bind(memoList[position])
            })*/
            holder.memoBtn.setOnClickListener {
                /*var bundle = Bundle()
                bundle.putString("name",memoViewModel.memoList[position].name)
                val memoDetailFragment = MemoDetailFragment()
                memoDetailFragment.arguments = bundle
                memoDetailFragment.show(requireFragmentManager(),"Dialog Fragment")
                 */
                var intent = Intent(requireContext(),MemoDetailActivity::class.java)
                intent.putExtra("name",memoList[position].name)
                startActivity(intent)
            }
        }
    }

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

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}