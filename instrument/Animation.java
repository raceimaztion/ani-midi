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
	protected InstrumentPart pieceAffected;
	// TODO: fill this in
	
	public Animation(InstrumentPart pieceAffected)
	{
		this.pieceAffected = pieceAffected;
	}
	
	public abstract boolean animate(float dTime);
}
