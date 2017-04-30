package com.fsbtech.interviews.entities;

import java.io.Serializable;

public class MarketRefType implements Serializable
{
    private final Integer marketRefId;
    private final String marketRefName;

    public MarketRefType(Integer marketRefId, String marketRefName)
    {
        this.marketRefId = marketRefId;
        this.marketRefName = marketRefName;
    }

    public Integer getMarketRefId() 
    {
        return marketRefId;
    }

    public String getMarketRefName() 
    {
        return marketRefName;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((marketRefId == null) ? 0 : marketRefId.hashCode());
		result = prime * result + ((marketRefName == null) ? 0 : marketRefName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarketRefType other = (MarketRefType) obj;
		if (marketRefId == null) {
			if (other.marketRefId != null)
				return false;
		} else if (!marketRefId.equals(other.marketRefId))
			return false;
		if (marketRefName == null) {
			if (other.marketRefName != null)
				return false;
		} else if (!marketRefName.equals(other.marketRefName))
			return false;
		return true;
	}
}
