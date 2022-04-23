package Izaac.Doyle.PubsApp.ui.Group



import Izaac.Doyle.PubsApp.databinding.GroupJoiningGroupBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomJoinGroupFragment:BottomSheetDialogFragment() {

    private var _binding: GroupJoiningGroupBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GroupJoiningGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root





        return  root
    }


}