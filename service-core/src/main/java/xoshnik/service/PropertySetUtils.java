package xoshnik.service;

import com.siebel.data.SiebelPropertySet;
import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class PropertySetUtils {

	public Boolean isEqualPropertySets(SiebelPropertySet firstPS, SiebelPropertySet secondPS) {
		return comparePropSets(firstPS, secondPS);
	}

	public Stream<String> getPropertyNamesAsStream(SiebelPropertySet ps) {
		return Stream.concat(Stream.of(ps.getFirstProperty()), Stream.generate(ps::getNextProperty))
				.limit(ps.getPropertyCount());
	}

	public Stream<SiebelPropertySet> getChildrenAsStream(SiebelPropertySet ps) {
		return IntStream.range(0, ps.getChildCount()).mapToObj(ps::getChild);
	}

	private boolean comparePropSets(SiebelPropertySet firstPS, SiebelPropertySet secondPS) {
		return compareHeaders(firstPS, secondPS)
				&& compareProperties(firstPS, secondPS)
				&& compareChildren(firstPS, secondPS);
	}

	private boolean compareHeaders(SiebelPropertySet firstPS, SiebelPropertySet secondPS) {
		return firstPS.getType().equals(secondPS.getType()) && firstPS.getValue().equals(secondPS.getValue());
	}

	private boolean compareProperties(SiebelPropertySet firstPS, SiebelPropertySet secondPS) {
		return firstPS.getPropertyCount() == 0 && secondPS.getPropertyCount() == 0 || firstPS.getPropertyCount() == secondPS
				.getPropertyCount() && getPropertyNamesAsStream(firstPS)
				.allMatch(property -> secondPS.propertyExists(property) && firstPS.getProperty(property)
						.equals(secondPS.getProperty(property)));
	}

	private boolean compareChildren(SiebelPropertySet firstPS, SiebelPropertySet secondPS) {
		if (firstPS.getChildCount() == 0 && secondPS.getChildCount() == 0) {
			return true;
		}
		if (firstPS.getChildCount() != secondPS.getChildCount()) {
			return false;
		}
		ArrayList<SiebelPropertySet> secondPsChildren = new ArrayList<>();
		ArrayList<SiebelPropertySet> used = new ArrayList<>();
		getChildrenAsStream(secondPS).forEach(secondPsChildren::add);
		return getChildrenAsStream(firstPS).allMatch(first -> secondPsChildren.stream().anyMatch(second -> {
			boolean result = false;
			if (!used.contains(second)) {
				result = comparePropSets(first, second);
				if (result) {
					used.add(second);
				}
			}
			return result;
		}));
	}

}
