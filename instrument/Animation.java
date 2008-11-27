package instrument;

public abstract class Animation
{
	/**
	 * The total amount of time that this animation lasts for
	 */
	protected float totalTime;
	/**
	 * The piece that this animation affects
	 */
	protected InstrumentPart part;
	// TODO: fill this in
	
	public Animation(InstrumentPart pieceAffected)
	{
		this.part = pieceAffected;
	}
	
	public InstrumentPart getAffectedPiece()
	{
		return part;
	}
	
	public abstract boolean animate(float dTime, float curTime);
}
