/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.util.demangler.swift.nodes;

import ghidra.app.util.bin.format.swift.SwiftTypeMetadata;
import ghidra.app.util.demangler.*;
import ghidra.app.util.demangler.swift.SwiftDemangledNodeKind;
import ghidra.app.util.demangler.swift.SwiftDemangler;
import ghidra.app.util.demangler.swift.datatypes.SwiftFunction;
import ghidra.program.model.lang.CompilerSpec;

/**
 * A {@link SwiftDemangledNodeKind#TypeMetadataAccessFunction} {@link SwiftNode}
 */
public class SwiftTypeMetadataAccessFunctionNode extends SwiftNode {

	@Override
	public Demangled demangle(SwiftDemangler demangler, SwiftTypeMetadata typeMetadata)
			throws DemangledException {
		String name = null;
		Demangled namespace = null;
		for (SwiftNode child : getChildren()) {
			switch (child.getKind()) {
				case Type:
					namespace = child.demangle(demangler, typeMetadata);
					name = "typeMetadataAccessor";
					break;
				default:
					skip(child);
					break;
			}
		}
		if (name == null) {
			return getUnknown();
		}
		SwiftFunction function =
			new SwiftFunction(properties.mangled(), properties.originalDemangled(), name, namespace,
				CompilerSpec.CALLING_CONVENTION_default);
		if (namespace instanceof DemangledDataType type) {
			function.setReturnType(type);
		}
		return function;

	}
}
