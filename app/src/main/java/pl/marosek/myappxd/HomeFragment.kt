package pl.marosek.myappxd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {

//    private lateinit var someText : EditText
//    private lateinit var textLabel : TextView
    var recyclerView : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)

        //recycler view



//        someText = view.findViewById(R.id.secondEdit)
//        textLabel = view.findViewById(R.id.secondView)
//
//        val clickmee = view.findViewById<Button>(R.id.firstOptioon)
//        clickmee.setOnClickListener {
//            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
//        }
//        val guzikjakis = view.findViewById<Button>(R.id.secondOption)
//
//        guzikjakis.setOnClickListener {
//            textLabel.setText(someText.getText())
//        }
    }




}