package page.info;

import common.util.stage.Stage;
import main.MainBCU;
import page.JTF;
import page.Page;
import utilpc.UtilPC;

import javax.swing.*;
import java.util.List;
import java.util.Vector;

public class StageFilterPage extends StagePage {

	private static final long serialVersionUID = 1L;

	private final JTF srch = new JTF();
	private final Stage[] stages;
	private final JList<Stage> jlst = new JList<>();
	private final JScrollPane jspst = new JScrollPane(jlst);

	public StageFilterPage(Page p, List<Stage> ls) {
		super(p);

		jlst.setListData(stages = ls.toArray(new Stage[0]));
		ini();
	}

	@Override
	protected void resized(int x, int y) {
		super.resized(x, y);

		set(srch, x, y, 400, 500, 300, 50);
		set(jspst, x, y, 400, 550, 300, 650);
		set(strt, x, y, 400, 0, 300, 50);
	}

	@Override
	public void callBack(Object v) {
		if (v instanceof Integer)
			super.setData(jlst.getSelectedValue(), (int) v);
	}

	private void addListeners() {

		jlst.addListSelectionListener(arg0 -> {
			if (arg0.getValueIsAdjusting())
				return;
			Stage s = jlst.getSelectedValue();
			if (s == null)
				return;
			setData(s, 0);
		});

		srch.setTypeLnr(x -> {
			Vector<Stage> filtered = new Vector<>();
			String text = srch.getText().toLowerCase();
			int minDiff = MainBCU.searchTolerance;
			for (Stage st : stages) {
				int diff = UtilPC.damerauLevenshteinDistance(st.toString().toLowerCase(), text);
				minDiff = Math.min(minDiff, diff);
				if (diff == minDiff)
					filtered.add(st);
			}
			Stage curr = jlst.getSelectedValue();
			jlst.setListData(filtered);
			jlst.setSelectedIndex(Math.max(filtered.indexOf(curr), 0));
		});

	}

	private void ini() {
		add(srch);
		add(jspst);
		addListeners();

	}

}
