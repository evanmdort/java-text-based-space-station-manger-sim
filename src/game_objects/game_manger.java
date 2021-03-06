package game_objects;

import java.io.Serializable;
import java.util.ArrayList;

import db.event_db;
import db.item_db;
import db.upgrades_db;
import helpers.S;
import helpers.ydb_s;

public class game_manger implements Serializable {
	
	public static market m;
	public static player p;
	
	
	public static void init_dbs() 
	{
		//init item db
		item_db.gen_items();
		//init events
		event_db.gen_events();
		//init upgrades
		upgrades_db.gen_upgrades();
	}
	public static void start_game() 
	{

		init_dbs();
		
		//init market
		m = new market(item_db.db);
		m.supply_and_demend_gen();
		
		//init player
		p = new player();
		p.credits = 1000;

	}//end start_game
	
	
	public static void pass_turn() 
	{
		game_manger.p.turn++;
		//add events
		//shufel murket
		m.supply_and_demend_gen();
		//update player
		p.update_taxes();
		
		//genrate events
		event_manger.random_events();
		//hendale long term events
		event_manger.long_term_events_update();
		
		//tax return
		if(p.taxes_owed<0) 
		{
			game_manger.p.events.add("tax return,"+(p.taxes_owed*-1)+", ");
			game_manger.p.credits += p.taxes_owed*-1;
		}
		
	}//end pass_turn

	public static void save_game() 
	{
		ArrayList a = new ArrayList();
		a.add(game_manger.p);
		a.add(game_manger.m);
		a.add(5);
		ydb_s.write_to_file("save_tst.dat", a);
	}//end save_game
	
	public static void load_game() 
	{
		init_dbs();
		ArrayList a = (ArrayList)ydb_s.read_file("save_tst.dat");
		game_manger.p = (player)a.get(0);
		game_manger.m = (market)a.get(1);
		
	}//end save_game
}
