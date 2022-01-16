package com.example.todolist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.databinding.MemoItemBinding
import com.example.todolist.model.Memo
import com.example.todolist.viewmodel.MemoViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.NullPointerException

class HomeFragment : Fragment() {

    lateinit var navController: NavController
    private var binding: FragmentHomeBinding? = null
    lateinit var memoViewModel: MemoViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        memoViewModel = ViewModelProvider(requireActivity()).get(MemoViewModel::class.java)
        binding?.memoRecyclerView?.adapter = CustomAdapter()
        binding?.memoRecyclerView?.layoutManager = LinearLayoutManager(activity)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding?.todoBtn?.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_todoFragment)
        }
    }

    inner class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ItemViewHolder>() {

        var memoList: ArrayList<Memo> = arrayListOf()

        init {
            memoViewModel.db?.collection("memo")?.addSnapshotListener { value, error ->
                for (snapshot in value!!.documents) {
                    memoList.add(snapshot.toObject(Memo::class.java)!!)     //파이어베이스에서 가져온값을 memoList에 넣음
                }
                notifyDataSetChanged()
            }
        }

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
            return memoList.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            memoViewModel.currentValue.observe(requireActivity(), Observer {
                holder.bind(it[position])
                //re_text.text = it[position].name
            })
            holder.memoBtn.setOnClickListener {
                /*val bottomSheetView = layoutInflater.inflate(R.layout.fragment_memo_detail,null)
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bundle.putString("name",memoViewModel.memoList[position].name)
                bundle.putString("time",memoViewModel.memoList[position].time)
                bottomSheetDialog.set
                bottomSheetDialog.setContentView(bottomSheetView)//바텀 쉬트뷰
                bottomSheetDialog.show()*/
                var bundle = Bundle()
                bundle.putString("name",memoViewModel.memoList[position].name)
                val memoDetailFragment = MemoDetailFragment()
                memoDetailFragment.arguments = bundle
                memoDetailFragment.show(requireFragmentManager(),"Dialog Fragment")
            }
        }
    }


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}