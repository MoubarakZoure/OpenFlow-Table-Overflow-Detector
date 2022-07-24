package fl.rest.ressources.OpenFlowStats.flowsRemoved;

import fl.rest.ressources.OpenFlowStats.Counters.CounterTable;

public class FlowRemovedTable extends CounterTable{
	
	
	
	private String matching ;
	private String match_hash ;
	private int duration_sec;
	private long duration_nsec ;
	private int byteCount;
	private int packetCount;
	private int idleTimeout;
	private int hardTimeout ;

	public FlowRemovedTable(String dpid, String tableID, long time_stamp,
			long total_count) {
		super(dpid, tableID, time_stamp, total_count);
		// TODO Auto-generated constructor stub
	}
	
	
	
	

	@Override
	public String toString() {
		return "FlowRemovedTable [matching=" + matching + ", match_hash="
				+ match_hash + ", duration_sec=" + duration_sec
				+ ", duration_nsec=" + duration_nsec + ", byteCount="
				+ byteCount + ", packetCount=" + packetCount + ", idleTimeout="
				+ idleTimeout + ", hardTimeout=" + hardTimeout + "]";
	}





	public FlowRemovedTable(String dpid, String tableID, long time_stamp,
			long total_count, String matching, String match_hash,
			int duration_sec, long duration_nsec, int byteCount,
			int packetCount, int idleTimeout, int hardTimeout) {
		super(dpid, tableID, time_stamp, total_count);
		this.matching = matching;
		this.match_hash = match_hash;
		this.duration_sec = duration_sec;
		this.duration_nsec = duration_nsec;
		this.byteCount = byteCount;
		this.packetCount = packetCount;
		this.idleTimeout = idleTimeout;
		this.hardTimeout = hardTimeout;
	}





	public String getMatching() {
		return matching;
	}

	public void setMatching(String matching) {
		this.matching = matching;
	}

	public String getMatch_hash() {
		return match_hash;
	}

	public void setMatch_hash(String match_hash) {
		this.match_hash = match_hash;
	}

	public int getDuration_sec() {
		return duration_sec;
	}

	public void setDuration_sec(int duration_sec) {
		this.duration_sec = duration_sec;
	}

	public long getDuration_nsec() {
		return duration_nsec;
	}

	public void setDuration_nsec(long duration_nsec) {
		this.duration_nsec = duration_nsec;
	}

	public int getByteCount() {
		return byteCount;
	}

	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}

	public int getPacketCount() {
		return packetCount;
	}

	public void setPacketCount(int packetCount) {
		this.packetCount = packetCount;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getHardTimeout() {
		return hardTimeout;
	}

	public void setHardTimeout(int hardTimeout) {
		this.hardTimeout = hardTimeout;
	}
	
	

}
